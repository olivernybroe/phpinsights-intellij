package com.phpinsights.phpinsights;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.NotNull;

public class ToolMessage {
    public int column;
    public int length;
    private final StringBuilder messageText;
    private final TextRange textRange;
    @NotNull
    private final ToolMessage.Severity severity;
    @NotNull
    private HighlightDisplayLevel displayLevel;

    @NotNull
    public HighlightDisplayLevel getDisplayLevel() {
        return this.displayLevel;
    }

    void setDisplayLevel(@NotNull HighlightDisplayLevel displayLevel) {
        this.displayLevel = displayLevel;
    }

    public ToolMessage(MessageProcessor messageProcessor, int lineNum, ToolMessage.Severity severity, String messageText) {
        this.displayLevel = HighlightDisplayLevel.ERROR;
        this.textRange = this.lineNumberToRange(lineNum - 1, messageProcessor);
        this.severity = severity != null ? severity : ToolMessage.Severity.WARNING;
        this.messageText = new StringBuilder(messageText);
    }

    private TextRange lineNumberToRange(int lineNum, MessageProcessor messageProcessor) {
        Document doc = PsiDocumentManager.getInstance(messageProcessor.getFile().getProject()).getDocument(messageProcessor.getFile());
        if (doc != null && lineNum >= 0 && lineNum < doc.getLineCount()) {
            int startOffset = doc.getLineStartOffset(lineNum) + this.column;
            int endOffset = this.length > 0 ? startOffset + this.length : doc.getLineEndOffset(lineNum);
            return TextRange.create(startOffset, endOffset);
        } else {
            return TextRange.EMPTY_RANGE;
        }
    }

    public TextRange getTextRange() {
        return this.textRange;
    }

    public String getMessageText() {
        return this.messageText.toString();
    }

    public boolean isInternalError() {
        return Severity.INTERNAL_ERROR.equals(this.severity);
    }

    public enum Severity {
        INTERNAL_ERROR,
        ERROR,
        WARNING;

        Severity() {
        }
    }
}
