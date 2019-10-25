package com.phpinsights.phpinsights;

import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

public class OutputProcessor {
    private static final Logger LOG = Logger.getInstance(OutputProcessor.class);
    private final StringBuffer errOutput;
    private final MessageProcessor myMessageProcessor;
    private int myLineCount;

    OutputProcessor(@NotNull MessageProcessor messageProcessor) {
        super();
        this.errOutput = new StringBuffer();
        this.myLineCount = 0;
        this.myMessageProcessor = messageProcessor;
    }

    void notifyTextAvailable(String text, Key outputType) {
        if (outputType == ProcessOutputTypes.STDOUT) {
            ++this.myLineCount;
            this.myMessageProcessor.parseLine(text);
        } else if (outputType == ProcessOutputTypes.STDERR) {
            this.processError(text);
        }

    }

    private void processError(String text) {
        this.errOutput.append(text);
    }

    void notifyProcessTerminated(int exitCode) {
        LOG.debug("Process finished with exit code " + exitCode + ", stdout line count = " + this.myLineCount + ", messages = " + this.myMessageProcessor.getMessageCount());

        this.myMessageProcessor.done();

        if (exitCode != 0 && this.errOutput.length() > 0) {
            this.myMessageProcessor.addInternalMessage(1, this.errOutput.toString());
        }

    }
}
