package com.phpinsights.phpinsights;

import com.google.gson.JsonElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import com.jetbrains.php.tools.quality.QualityToolsComposerConfig;
import org.jetbrains.annotations.NotNull;

public class PhpInsightsComposerConfig extends QualityToolsComposerConfig<PhpInsightsConfiguration, PhpInsightsInspection> {
    private static final String PACKAGE = "nunomaduro/phpinsights";
    private static final String RELATIVE_PATH;
    private static final PhpInsightsInspection PHP_INSIGHTS_VALIDATION_INSPECTION;

    public PhpInsightsComposerConfig() {
        super(PACKAGE, RELATIVE_PATH);
    }

    @NotNull
    protected QualityToolConfigurationManager<PhpInsightsConfiguration> getConfigurationManager(@NotNull Project project) {
        return PhpInsightsConfigurationManager.getInstance(project);
    }

    protected void checkComposerScriptsLeaves(JsonElement element, Ref<String> result) {
    }

    protected ComposerLogMessageBuilder.Settings getQualityToolsSettings() {
        //TODO: make our own settings page
        return ComposerLogMessageBuilder.Settings.MESS_DETECTOR;
    }

    protected ComposerLogMessageBuilder.Settings getQualityToolsInspectionSettings() {
        //TODO: make our own settings page
        return ComposerLogMessageBuilder.Settings.MESS_DETECTOR_INSPECTION;
    }

    protected PhpInsightsInspection getQualityInspection() {
        return PHP_INSIGHTS_VALIDATION_INSPECTION;
    }

    static {
        RELATIVE_PATH = "bin/phpinsights";
        PHP_INSIGHTS_VALIDATION_INSPECTION = new PhpInsightsInspection();
    }
}
