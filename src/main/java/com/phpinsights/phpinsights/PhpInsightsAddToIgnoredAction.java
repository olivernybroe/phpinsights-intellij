package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolAddToIgnoredAction;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NotNull;

public class PhpInsightsAddToIgnoredAction extends QualityToolAddToIgnoredAction {

    @Override
    protected @NotNull QualityToolType<PhpInsightsConfiguration> getQualityToolType(Project project) {
        return PhpInsightsQualityToolType.INSTANCE;
    }
}