package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolsIgnoreFilesConfigurable;
import org.jetbrains.annotations.NotNull;

public class PhpInsightsIgnoredFilesConfigurable extends QualityToolsIgnoreFilesConfigurable {
    public PhpInsightsIgnoredFilesConfigurable(Project project) {
        super(PhpInsightsQualityToolType.INSTANCE, project);
    }

    @NotNull
    public String getId() {
        return PhpInsightsIgnoredFilesConfigurable.class.getName();
    }
}
