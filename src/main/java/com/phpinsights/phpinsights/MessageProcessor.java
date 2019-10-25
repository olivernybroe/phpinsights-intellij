package com.phpinsights.phpinsights;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class MessageProcessor {
    private static final Gson gson = new Gson();
    private final PhpInsightsInspection inspection;

    private final List<ToolMessage> messages = new ArrayList<>();
    private final PsiFile myFile;
    private ToolMessage internalErrorMessage;
    private boolean myFatalError = false;
    private final HighlightDisplayLevel defaultHighlightDisplayLevel;
    private String tempOutput = "";

    MessageProcessor(AnnotatorInfo info) {
        this.defaultHighlightDisplayLevel = HighlightDisplayLevel.WARNING;
        this.myFile = info.getPsiFile();
        this.inspection = info.getInspection();
    }

    public PsiFile getFile() {
        return this.myFile;
    }

    @NotNull
    public final List<ToolMessage> getMessages() {
        return this.messages;
    }

    public int getMessageCount() {
        return this.messages.size();
    }

    public final void addInternalMessage(int line, String messageText) {
        this.addMessage(new ToolMessage(this, line, ToolMessage.Severity.INTERNAL_ERROR, messageText));
    }

    protected void addMessage(ToolMessage message) {
        if (message.isInternalError()) {
            this.internalErrorMessage = message;
        }

        message.setDisplayLevel(this.defaultHighlightDisplayLevel);
        this.messages.add(message);
    }

    public ToolMessage getInternalErrorMessage() {
        return this.internalErrorMessage;
    }

    public void setFatalError() {
        this.myFatalError = true;
    }

    boolean isFatalError() {
        return this.myFatalError;
    }

    void parseLine(String line) {
        tempOutput += line;
    }

    void done() {
        try {
            PhpInsightJson json = gson.fromJson(tempOutput, PhpInsightJson.class);

            Stream.of(
                json.architecture,
                json.code,
                json.complexity,
                json.security,
                json.style
            ).flatMap(Collection::stream)
                .map((this::toMessage))
                .forEach(this::addMessage);
        } catch (JsonSyntaxException exception) {
            Notifications.Bus.notify(
                new Notification(
                    PhpInsights.NOTIFICATION_GROUP_DISPLAY_ID.toString(),
                    PhpInsightsBundle.message("JSON_PARSE_FAILED_TITLE"),
                    exception.getMessage() + "<br><br><code>"+ tempOutput + "</code>",
                    NotificationType.ERROR
                )
            );
        } finally {
            tempOutput = "";
        }
    }

    @NotNull
    private ToolMessage toMessage(Insight insight) {
        String text = String.format("%s: %s", insight.title, insight.message);

        if (this.inspection.SHOW_INSIGHT_NAMES) {
            text = String.format("%s: %s", insight.insightClass, text);
        }

        return new ToolMessage(
            this,
            insight.line,
            ToolMessage.Severity.WARNING,
            text
        );
    }
}
