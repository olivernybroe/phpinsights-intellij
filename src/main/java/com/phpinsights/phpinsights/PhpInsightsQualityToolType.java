package com.phpinsights.phpinsights;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsQualityToolType extends QualityToolType {
    public static final PhpInsightsQualityToolType INSTANCE = new PhpInsightsQualityToolType();
    
    @Override
    public @NotNull String getDisplayName() {
        return PhpInsights.TOOL_NAME.toString();
    }

    @Override
    public @NotNull QualityToolBlackList getQualityToolBlackList(@NotNull Project project) {
        return PhpInsightsBlackList.getInstance(project);
    }

    @Override
    protected @NotNull QualityToolConfigurationManager getConfigurationManager(@NotNull Project project) {
        return null;
    }

    @Override
    protected @NotNull QualityToolValidationInspection getInspection() {
        return null;
    }

    @Override
    protected @Nullable QualityToolConfigurationProvider getConfigurationProvider() {
        return null;
    }

    @Override
    protected @NotNull QualityToolConfigurableForm createConfigurableForm(@NotNull Project project, QualityToolConfiguration qualityToolConfiguration) {
        return null;
    }

    @Override
    protected @NotNull Configurable getToolConfigurable(@NotNull Project project) {
        return null;
    }

    @Override
    protected @NotNull QualityToolProjectConfiguration getProjectConfiguration(@NotNull Project project) {
        return null;
    }

    @NotNull
    @Override
    protected QualityToolConfiguration createConfiguration() {
        return null;
    }
}
