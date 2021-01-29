package com.phpinsights.phpinsights;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsQualityToolType extends QualityToolType<PhpInsightsConfiguration> {
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
    protected @NotNull QualityToolConfigurationManager<PhpInsightsConfiguration> getConfigurationManager(@NotNull Project project) {
        return PhpInsightsConfigurationManager.getInstance(project);
    }

    @Override
    protected @NotNull QualityToolValidationInspection getInspection() {
        return new PhpInsightsValidationInspection();
    }

    @Override
    protected @Nullable QualityToolConfigurationProvider<PhpInsightsConfiguration> getConfigurationProvider() {
        return null;
    }

    @Override
    protected @NotNull QualityToolConfigurableForm<PhpInsightsConfiguration> createConfigurableForm(@NotNull Project project, PhpInsightsConfiguration phpInsightsConfiguration) {
        return null;
    }

    @Override
    protected @NotNull Configurable getToolConfigurable(@NotNull Project project) {
        return null;
    }

    @Override
    protected @NotNull QualityToolProjectConfiguration<PhpInsightsConfiguration> getProjectConfiguration(@NotNull Project project) {
        return null;
    }

    @NotNull
    @Override
    protected PhpInsightsConfiguration createConfiguration() {
        return null;
    }
}
