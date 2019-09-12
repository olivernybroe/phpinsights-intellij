package com.phpinsights.phpinsights;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.interpreters.PhpSdkFileTransfer;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PhpInsightsAnnotator extends QualityToolAnnotator {
    private static final Logger LOG = Logger.getInstance(PhpInsightsAnnotator.class);
    private static final String TEMP_DIRECTORY = "phpinsights_temp.tmp";
    static final PhpInsightsAnnotator INSTANCE = new PhpInsightsAnnotator();

    public PhpInsightsAnnotator() {
    }

    @Nullable
    protected QualityToolConfiguration getConfiguration(@NotNull Project project, @NotNull LocalInspectionTool inspection) {
        try {
            return PhpInsightsProjectConfiguration.getInstance(project).findSelectedConfiguration(project);
        } catch (QualityToolValidationException ignored) {
            return null;
        }
    }

    @NotNull
    protected String getTemporaryFilesFolder() {
        return TEMP_DIRECTORY;
    }

    @NotNull
    protected String getInspectionId() {
        return (new PhpInsightsInspection()).getID();
    }

    protected void runTool(@NotNull QualityToolMessageProcessor messageProcessor, @NotNull QualityToolAnnotatorInfo annotatorInfo, @NotNull PhpSdkFileTransfer transfer) throws ExecutionException {
        LOG.info("runTool");
        List<String> params = new ArrayList<>();
        params.add("analyse");
        params.add(annotatorInfo.getFilePath());
        params.add("--format=json");
        LOG.info(params.toString());
        LOG.info(annotatorInfo.getToolPath());


        PhpInsightsBlackList blackList = PhpInsightsBlackList.getInstance(annotatorInfo.getProject());
        QualityToolProcessCreator.runToolProcess(
            annotatorInfo,
            blackList,
            messageProcessor,
            null,
            transfer,
            params
        );

        if (messageProcessor.getInternalErrorMessage() != null) {
            if (annotatorInfo.isOnTheFly()) {
                String message = messageProcessor.getInternalErrorMessage().getMessageText();
                showProcessErrorMessage(annotatorInfo, blackList, message);
            }

            messageProcessor.setFatalError();
        }
    }

    protected QualityToolMessageProcessor createMessageProcessor(@NotNull QualityToolAnnotatorInfo collectedInfo) {

        PhpInsightsInspection inspection = (PhpInsightsInspection) collectedInfo.getInspection();
        return new PhpInsightsMessageProcessor(
            collectedInfo.getMaxMessagesPerFile(),
            collectedInfo,
            inspection
        );
    }
}
