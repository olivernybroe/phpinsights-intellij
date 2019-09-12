package com.phpinsights.phpinsights;

import com.google.gson.Gson;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo;
import com.jetbrains.php.tools.quality.QualityToolMessage;
import com.jetbrains.php.tools.quality.QualityToolMessageProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Stream;

import static com.intellij.internal.psiView.PsiViewerDialog.LOG;

public class PhpInsightsMessageProcessor extends QualityToolMessageProcessor {
    private static final Gson gson = new Gson();
    private PhpInsightsInspection inspection;

    PhpInsightsMessageProcessor(int maxMessages, QualityToolAnnotatorInfo info, PhpInsightsInspection inspection) {
        super(info, maxMessages);
        this.inspection = inspection;
    }

    @Override
    public void parseLine(String line) {
        PhpInsightJson json = gson.fromJson(line, PhpInsightJson.class);

        Stream.of(
            json.architecture,
            json.code,
            json.complexity,
            json.security,
            json.style
        ).flatMap(Collection::stream)
            .map((this::toMessage))
            .forEach(this::addMessage);
    }

    @Override
    public void done() {
        LOG.info("Message processor done.");
    }

    @Nullable
    @Override
    protected HighlightDisplayLevel severityToDisplayLevel(@NotNull QualityToolMessage.Severity severity) {
        return null;
    }

    @NotNull
    @Override
    protected String getQuickFixFamilyName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    @Nullable
    @Override
    protected Configurable getToolConfigurable(@NotNull Project project) {
        return new PhpInsightsConfigurable(project);
    }

    @NotNull
    private QualityToolMessage toMessage(Insight insight) {
        String text = String.format("%s: %s", insight.title, insight.message);

        if (this.inspection.SHOW_INSIGHT_NAMES) {
            text = String.format("%s: %s", insight.insightClass, text);
        }

        return new QualityToolMessage(
            this,
            insight.line,
            QualityToolMessage.Severity.WARNING,
            text
        );
    }
}
