package com.phpinsights.phpinsights;

import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInspection.InspectionProfile;
import com.intellij.codeInspection.SuppressionUtil;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.execution.ExecutionException;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.util.ArrayUtilRt;
import com.jetbrains.php.config.interpreters.*;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.tools.quality.QualityToolBlackList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhpInsightsAnnotator extends ExternalAnnotator<AnnotatorInfo, MessageProcessor> {
    private static final Logger LOG = Logger.getInstance(PhpInsightsAnnotator.class);
    static final PhpInsightsAnnotator INSTANCE = new PhpInsightsAnnotator();

    @NonNls private static final String TEMP_DIRECTORY = "phpinsights_temp.tmp";
    @NonNls private static final String GROUP_ID = "PHP External Quality Tools";
    @NonNls private static final String START_COMMAND = "analyse";
    @NonNls private static final String JSON_FORMAT_PARAMETER = "--format=json";
    @NonNls private static final String CONFIG_PATH_PARAMETER = "--config-path=%s";

    AnnotatorInfo collectAnnotatorInfo(@NotNull PsiFile file) {
        InspectionProfile inspectionProfile = InspectionProjectProfileManager.getInstance(file.getProject()).getCurrentProfile();
        String id = this.getInspectionId();
        LocalInspectionToolWrapper localInspectionToolWrapper = (LocalInspectionToolWrapper) inspectionProfile.getInspectionTool(id, file);
        if (localInspectionToolWrapper != null && (inspectionProfile.isToolEnabled(HighlightDisplayKey.find(id), file))) {
            PhpInsightsInspection tool = (PhpInsightsInspection) localInspectionToolWrapper.getTool();
            if (!this.isFileSuitable(file)) {
                return null;
            } else if (SuppressionUtil.inspectionResultSuppressed(file, tool)) {
                return null;
            } else {
                Project project = file.getProject();
                PhpInsightsConfiguration configuration = PhpInsightsConfiguration.getInstance(project);
                if (configuration != null && !StringUtil.isEmpty(configuration.getToolPath())) {
                    return new AnnotatorInfo(file, tool, project, configuration, true);
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    private boolean isFileSuitable(@NotNull PsiFile file) {
        return file instanceof PhpFile && file.getViewProvider().getBaseLanguage() == PhpLanguage.INSTANCE && file.getContext() == null;
    }

    @Nullable
    public AnnotatorInfo collectInformation(@NotNull PsiFile file, @NotNull Editor editor, boolean hasErrors) {
        return hasErrors ? null : this.collectAnnotatorInfo(file);
    }

    @Nullable
    public final MessageProcessor doAnnotate(AnnotatorInfo collectedInfo) {
        if (!this.isContextValid(collectedInfo)) {
            return null;
        } else {
            PhpSdkFileTransfer transfer = PhpSdkFileTransfer.getSdkFileTransfer(null);
            ProgressManager.checkCanceled();

            try {
                this.createTempFile(collectedInfo, transfer);
            } catch (ExecutionException exception) {
                showProcessErrorMessage(
                    collectedInfo,
                    StringUtil.notNullize(
                        exception.getMessage(),
                        PhpInsightsBundle.message("FAILED_CREATING_TEMPORARY_FILE")
                    )
                );
                return null;
            } catch (IOException exception) {
                showProcessErrorMessage(collectedInfo, exception.getMessage());
                return null;
            }

            ProgressManager.checkCanceled();
            MessageProcessor messageProcessor = this.createMessageProcessor(collectedInfo);

            try {
                this.runTool(messageProcessor, collectedInfo);
            } catch (ExecutionException exception) {
                showProcessErrorMessage(collectedInfo, exception.getMessage());
            } finally {
                try {
                    removeTempFile(collectedInfo, transfer);
                } catch (ExecutionException exception) {
                    showProcessErrorMessage(collectedInfo, exception.getMessage());
                }

            }

            return messageProcessor.isFatalError() ? null : messageProcessor;
        }
    }

    private boolean isContextValid(AnnotatorInfo collectedInfo) {
        return collectedInfo != null && !collectedInfo.getProject().isDisposed() && (new ContextValidator()).isValid(collectedInfo.getPsiFile()) && collectedInfo.getPsiFile().isValid();
    }

    private void createTempFile(@NotNull AnnotatorInfo collectedInfo, @NotNull PhpSdkFileTransfer transfer) throws IOException, ExecutionException {
        Project project = collectedInfo.getProject();
        String path = null;
        String basePath = project.getBasePath();
        String pathToOriginalFile = FileUtil.toSystemIndependentName(collectedInfo.getOriginalFile().getPath());
        if (StringUtil.isNotEmpty(basePath) && FileUtil.isAncestor(basePath, pathToOriginalFile, true)) {
            path = FileUtil.getRelativePath(basePath, pathToOriginalFile, '/');
        }

        if (StringUtil.isEmpty(path)) {
            path = collectedInfo.getOriginalFile().getName();
        }

        if (project.isDisposed()) {
            throw new ExecutionException("Cannot create file because project is already disposed");
        } else {
            String file = transfer.createFile(project, path, getFileContent(collectedInfo.getPsiFile()), TEMP_DIRECTORY, collectedInfo.getTimeout() / 2);
            collectedInfo.setTempFile(file);
        }
    }

    private static String getFileContent(@NotNull PsiFile file) {
        return ReadAction.compute(file::getText);
    }

    private static void removeTempFile(@NotNull AnnotatorInfo collectedInfo, @NotNull PhpSdkFileTransfer transfer) throws ExecutionException {
        String tempFile = collectedInfo.getFile();
        if (tempFile != null) {
            transfer.delete(collectedInfo.getProject(), collectedInfo.getTimeout() / 2, true);
        }
    }

    private static void showProcessErrorMessage(@NotNull final AnnotatorInfo collectedInfo, @NotNull String messageText) {
        if (collectedInfo.isOnTheFly() && !collectedInfo.getProject().isDisposed()) {
            final PhpInsightsInspection inspection = collectedInfo.getInspection();
            String text = textToHtml(messageText) + PhpInsightsBundle.message("DISABLE_INSPECTION_MESSAGE");
            NotificationListener listener = new NotificationListener() {
                public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
                    Project project = collectedInfo.getProject();
                    if (!project.isDisposed()) {
                        InspectionProfileManager.getInstance(project).getCurrentProfile().setToolEnabled(inspection.getID(), false);
                        this.showInfoNotification(PhpInsightsBundle.message("INSPECTION_WAS_DISABLED", inspection.getDisplayName()));
                    }
                }

                private void showInfoNotification(@NotNull String info) {
                    Notifications.Bus.notify(new Notification(GROUP_ID, inspection.getToolName(), info, NotificationType.INFORMATION));
                }
            };
            showProcessErrorMessage(inspection, text, listener);
        } else {
            LOG.warn(messageText);
        }
    }

    private static String textToHtml(@NonNls @NotNull String text) {
        return text.replace("\n", "<br>");
    }

    private static void showProcessErrorMessage(@NotNull AnnotatorInfo collectedInfo, @NotNull QualityToolBlackList blackList, @NotNull String messageText) {
        PhpInsightsInspection inspection = collectedInfo.getInspection();
        String text = PhpInsightsBundle.message(
            "EXCLUDE_FILE_MESSAGE",
            textToHtml(messageText),
            collectedInfo.getOriginalFile().getName(),
            inspection.getToolName()
        );
        showProcessErrorMessage(inspection, text, (notification, event) -> {
            VirtualFile originalFile = collectedInfo.getOriginalFile();
            if (blackList.addFile(originalFile)) {
                Notifications.Bus.notify(new Notification(GROUP_ID, inspection.getToolName(), PhpInsightsBundle.message("FILE_IGNORED", originalFile.getName()), NotificationType.INFORMATION));
            }
        });
    }

    private static void showProcessErrorMessage(@NotNull PhpInsightsInspection inspection, @NotNull String messageText, @Nullable NotificationListener listener) {
        Notifications.Bus.notify(new Notification(GROUP_ID, inspection.getToolName(), messageText, NotificationType.ERROR, listener));
    }

    private static class ContextValidator extends PsiRecursiveElementVisitor {
        private boolean isValid;

        private ContextValidator() {
            this.isValid = true;
        }

        boolean isValid(PsiElement rootElement) {
            if (rootElement == null) {
                return true;
            } else {
                ApplicationManager.getApplication().runReadAction(() -> this.visitElement(rootElement));
                return this.isValid;
            }
        }

        public void visitElement(@NotNull PsiElement element) {
            if (this.isValid) {
                if (element instanceof PsiErrorElement) {
                    this.isValid = false;
                } else {
                    super.visitElement(element);
                }
            }
        }
    }

    @NotNull
    private String getInspectionId() {
        return (new PhpInsightsInspection()).getID();
    }

    private void runTool(@NotNull MessageProcessor messageProcessor, @NotNull AnnotatorInfo annotatorInfo) throws ExecutionException {
        List<String> params = new ArrayList<>();

        // The first argument to tell it that it is the analyse command we want to run.
        params.add(START_COMMAND);

        // Add the current file we are trying to analyse.
        params.add(annotatorInfo.getFilePath());

        // Use the JSON formatter, so we know how to output will look.
        params.add(JSON_FORMAT_PARAMETER);

        PhpInsightsInspection inspection = annotatorInfo.getInspection();

        // Add the config path to the parameters
        if (inspection.CONFIG_PATH != null) {
            params.add(String.format(CONFIG_PATH_PARAMETER, inspection.CONFIG_PATH));
        }

        // Run the command.
        PhpInsightsBlackList blackList = PhpInsightsBlackList.getInstance(annotatorInfo.getProject());

        ProcessCreator.runToolProcess(
            annotatorInfo.getToolPath(),
            annotatorInfo.getTimeout(),
            blackList,
            messageProcessor,
            null,
            annotatorInfo.getOriginalFile(),
            ArrayUtilRt.toStringArray(params)
        );

        // Show an error if the command fails.
        if (messageProcessor.getInternalErrorMessage() != null) {
            if (annotatorInfo.isOnTheFly()) {
                String message = messageProcessor.getInternalErrorMessage().getMessageText();
                showProcessErrorMessage(annotatorInfo, blackList, message);
            }

            messageProcessor.setFatalError();
        }
    }

    private MessageProcessor createMessageProcessor(@NotNull AnnotatorInfo collectedInfo) {
        return new MessageProcessor(collectedInfo);
    }
}
