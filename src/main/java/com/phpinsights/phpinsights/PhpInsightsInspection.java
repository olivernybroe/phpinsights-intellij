package com.phpinsights.phpinsights;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.tools.quality.QualityToolAnnotationAppender;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhpInsightsInspection extends LocalInspectionTool {
    public boolean SHOW_INSIGHT_NAMES;

    @Nullable
    public String CONFIG_PATH = null;

    public PhpInsightsInspection() {
        this.SHOW_INSIGHT_NAMES = false;
    }

    @NotNull
    public String[] getGroupPath() {
        return PhpInspection.GROUP_PATH_GENERAL;
    }

    @NotNull
    public String getShortName() {
        return this.getClass().getSimpleName();
    }

    @Nls
    @NotNull
    public String getDisplayName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    @Nullable
    public JComponent createOptionsPanel() {
        return (new PhpInsightsOptionsPanel(this)).getContentPane();
    }

    @NotNull
    protected PhpInsightsAnnotator getAnnotator() {
        return PhpInsightsAnnotator.INSTANCE;
    }

    public String getToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }

    /**
     * This is the start of the whole inspection.
     *
     * From here all the command is launched and the data is parsed and so on.
     */
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        BatchAnnotationTarget annotationTarget = new BatchAnnotationTarget(manager, file);
        PhpInsightsAnnotator annotator = this.getAnnotator();
        AnnotatorInfo info = annotator.collectAnnotatorInfo(file);
        if (info != null) {
            (new BatchAnnotationAppender(file, annotationTarget)).apply(annotator.doAnnotate(info));
        }

        return annotationTarget.getDescriptors();
    }

    private static class BatchAnnotationTarget {
        private final List<ProblemDescriptor> myDescriptors;
        private final InspectionManager myManager;
        private final PsiFile myFile;

        private BatchAnnotationTarget(InspectionManager manager, PsiFile file) {
            this.myDescriptors = new ArrayList<>();
            this.myManager = manager;
            this.myFile = file;
        }

        public void createAnnotation(HighlightDisplayLevel severity, TextRange range, String messageText, IntentionAction... actions) {
            ProblemHighlightType highlightType = ProblemHighlightType.WEAK_WARNING;
            if (severity == HighlightDisplayLevel.ERROR) {
                highlightType = ProblemHighlightType.ERROR;
            } else if (severity == HighlightDisplayLevel.WARNING) {
                highlightType = ProblemHighlightType.GENERIC_ERROR_OR_WARNING;
            }

            this.myDescriptors.add(
                this.myManager.createProblemDescriptor(
                    this.myFile,
                    range,
                    messageText,
                    highlightType,
                    false,
                    IntentionWrapper.wrapToQuickFixes(actions, this.myFile)
                )
            );
        }

        public void creteInternalErrorAnnotation(@NotNull String messageText) {
            this.myDescriptors.add(this.myManager.createProblemDescriptor(this.myFile, messageText, false, LocalQuickFix.EMPTY_ARRAY, ProblemHighlightType.ERROR));
        }

        public ProblemDescriptor[] getDescriptors() {
            return this.myDescriptors.toArray(ProblemDescriptor.EMPTY_ARRAY);
        }
    }

    private static class BatchAnnotationAppender  {
        private static final int INITIAL_MESSAGE_COUNTERS_CAPACITY = 100;
        private static final int MAX_MESSAGES_PER_LINE = 6;
        private final PsiFile myFile;
        private final BatchAnnotationTarget myTarget;
        private final HashMap<TextRange, Integer> myMessageCounts;
        private static final Logger LOG = Logger.getInstance(QualityToolAnnotationAppender.class);

        public BatchAnnotationAppender(PsiFile file, @NotNull BatchAnnotationTarget target) {
            this.myMessageCounts = new HashMap<>(INITIAL_MESSAGE_COUNTERS_CAPACITY);
            this.myFile = file;
            this.myTarget = target;
        }

        public void apply(MessageProcessor annotationResult) {
            if (annotationResult != null) {
                annotationResult.getMessages().forEach(this::addAnnotation);
            }
        }

        public void addAnnotation(ToolMessage message) {
            Document doc = PsiDocumentManager.getInstance(this.myFile.getProject()).getDocument(this.myFile);
            if (doc != null) {
                TextRange textRange = message.getTextRange();
                int messageCount = this.myMessageCounts.getOrDefault(textRange, 0) + 1;
                this.myMessageCounts.put(textRange, messageCount);
                if (message.isInternalError()) {
                    this.myTarget.creteInternalErrorAnnotation(message.getMessageText());
                } else {
                    if (textRange.getEndOffset() <= doc.getTextLength()) {
                        if (messageCount > MAX_MESSAGES_PER_LINE) {
                            return;
                        }

                        String messageText = messageCount < MAX_MESSAGES_PER_LINE ? message.getMessageText() : PhpInsightsBundle.message("TOO_MANY_MESSAGE");
                        this.myTarget.createAnnotation(message.getDisplayLevel(), textRange, messageText);
                    } else {
                        LOG.warn(PhpInsightsBundle.message("EXCEED_DOCUMENT_LENGTH", textRange, doc.getTextLength(), this.myFile.getVirtualFile().getPath()));
                    }

                }
            }
        }
    }

}
