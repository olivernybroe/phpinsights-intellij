package com.phpinsights.phpinsights;

import com.intellij.openapi.util.NlsSafe;
import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("InspectionDescriptionNotFoundInspection")
public class PhpInsightsValidationInspection extends QualityToolValidationInspection {
    @Override
    protected @NotNull QualityToolAnnotator getAnnotator() {
        return PhpInsightsAnnotatorProxy.INSTANCE;
    }

    @Override
    public @NlsSafe String getToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }
}
