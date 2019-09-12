package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolAddToIgnoredAction;
import com.jetbrains.php.tools.quality.QualityToolBlackList;
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm;
import com.jetbrains.php.tools.quality.QualityToolsIgnoreFilesConfigurable;
import org.jetbrains.annotations.NotNull;

public class PhpInsightsAddToIgnoredAction extends QualityToolAddToIgnoredAction {
    public PhpInsightsAddToIgnoredAction() {
    }

    @NotNull
    protected QualityToolBlackList getBlackList(Project project) {
        return PhpInsightsBlackList.getInstance(project);
    }

    @NotNull
    protected String getToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }

    @NotNull
    protected QualityToolProjectConfigurableForm getToolConfigurable(@NotNull Project project) {
        return new PhpInsightsConfigurable(project);
    }

    protected QualityToolsIgnoreFilesConfigurable getIgnoredFilesConfigurable(@NotNull Project project) {
        return new PhpInsightsIgnoredFilesConfigurable(project);
    }
}
