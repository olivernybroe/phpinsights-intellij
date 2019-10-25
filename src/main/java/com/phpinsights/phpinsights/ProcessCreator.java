package com.phpinsights.phpinsights;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ConcurrencyUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ProcessCreator {
    private static final Logger LOG = Logger.getInstance(ProcessCreator.class);
    private static final ExecutorService myToolExecutorService = Executors.newFixedThreadPool(5, ConcurrencyUtil.newNamedThreadFactory("PHP Insights executor"));

    @NotNull
    public static ProcessOutput getToolOutput(@NotNull String path, int timeout, @NotNull String... params) throws ExecutionException {
        return new CapturingProcessHandler(createGeneralCommandLine(
            path,
            null,
            params
        )).runProcess(timeout);
    }

    public static void runToolProcess(@NotNull String path,
                                      int timeout,
                                      @Nullable PhpInsightsBlackList blackList,
                                      @NotNull MessageProcessor messageProcessor,
                                      @Nullable String workingDirectory,
                                      @Nullable VirtualFile originalFile,
                                      String... params) throws ExecutionException {
        if (blackList == null || originalFile == null || !blackList.containsFile(originalFile)) {
            try {
                ProcessHandler handler = myToolExecutorService.submit(() -> {
                    ProcessHandler handler1 = createProcessHandler(path, messageProcessor, workingDirectory, params);
                    handler1.startNotify();
                    handler1.waitFor(2L * (long)timeout / 3L);
                    return handler1;
                }).get(timeout, TimeUnit.MILLISECONDS);
                if (!handler.isProcessTerminated()) {
                    handler.destroyProcess();
                    messageProcessor.addInternalMessage(1, getHangupMessage(path, timeout, params));
                }

            } catch (TimeoutException var11) {
                throw new ExecutionException(StringUtil.isEmpty(var11.getMessage()) ? "No response from " + path + " after " + timeout + " ms" : var11.getMessage());
            } catch (java.util.concurrent.ExecutionException | InterruptedException var12) {
                throw new ExecutionException(var12);
            }
        }
    }

    @NotNull
    private static String getHangupMessage(@NotNull String path, int timeout, String[] params) {
        LOG.info(PhpInsightsBundle.message("NO_RESPONSE_LOG_MESSAGE", path, timeout));

        return PhpInsightsBundle.message(
            "TOOL_FAILED_MESSAGE",
            Arrays.stream(params).map((param) -> "  " + param + "\n").collect(Collectors.joining()),
            timeout / 1000
        );
    }

    private static ProcessHandler createProcessHandler(@NotNull String path,
                                                       @NotNull MessageProcessor messageProcessor,
                                                       @Nullable String workingDirectory,
                                                       String... params) throws ExecutionException {
        GeneralCommandLine commandLine = createGeneralCommandLine(path, workingDirectory, params);
        return new ProcessHandler(commandLine, messageProcessor);
    }

    @NotNull
    private static GeneralCommandLine createGeneralCommandLine(@NotNull String path, @Nullable String workingDirectory, String[] params) {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        if (StringUtil.isNotEmpty(workingDirectory)) {
            commandLine.setWorkDirectory(workingDirectory);
        }

        commandLine.setExePath(path);
        commandLine.addParameters(params);
        return commandLine;
    }
}
