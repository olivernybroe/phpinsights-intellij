package com.phpinsights.phpinsights;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class MessageProcessor extends QualityToolMessageProcessor {
    private static final Gson gson = new Gson();

    private String tempOutput = "";

    MessageProcessor(QualityToolAnnotatorInfo info) {
        super(info);
    }

    @Override
    protected @IntentionFamilyName QualityToolType getQualityToolType() {
        return PhpInsightsQualityToolType.INSTANCE;
    }

    @Override
    public void parseLine(String line) {
        tempOutput += line;
    }

    @Override
    public void done() {
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
    private QualityToolMessage toMessage(Insight insight) {
        String text = String.format("%s: %s", insight.title, insight.message);

        return new QualityToolMessage(
            this,
            insight.line,
            QualityToolMessage.Severity.WARNING,
            text
        );
    }
}
