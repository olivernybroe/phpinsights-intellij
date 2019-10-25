package com.phpinsights.phpinsights;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

public class ProcessHandler extends OSProcessHandler {
    private final OutputProcessor myOutputProcessor;

    public ProcessHandler(@NotNull GeneralCommandLine commandLine, @NotNull MessageProcessor messageProcessor) throws ExecutionException {
        super(commandLine);
        this.myOutputProcessor = new OutputProcessor(messageProcessor);
    }

    public void notifyTextAvailable(@NotNull String text, @NotNull Key outputType) {
        this.myOutputProcessor.notifyTextAvailable(text, outputType);
    }

    protected void notifyProcessTerminated(int exitCode) {
        this.myOutputProcessor.notifyProcessTerminated(exitCode);
        super.notifyProcessTerminated(exitCode);
    }
}
