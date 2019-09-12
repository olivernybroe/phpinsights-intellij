package com.phpinsights.phpinsights;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox;
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm;
import com.jetbrains.php.tools.quality.QualityToolValidationException;
import com.jetbrains.php.tools.quality.QualityToolsIgnoreFilesConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsConfigurable extends QualityToolProjectConfigurableForm implements Configurable.NoScroll {
    PhpInsightsConfigurable(@NotNull Project project) {
        super(project);
    }

    @Nls
    public String getDisplayName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    public String getHelpTopic() {
        return PhpInsights.HELP_TOPIC.toString();
    }

    @NotNull
    public String getId() {
        return PhpInsightsConfigurable.class.getName();
    }

    protected void updateSelectedConfiguration(@Nullable String newConfigurationId) {
        PhpInsightsProjectConfiguration projectConfiguration = PhpInsightsProjectConfiguration.getInstance(this.myProject);
        if (newConfigurationId != null && !StringUtil.equals(newConfigurationId, projectConfiguration.getSelectedConfigurationId())) {
            projectConfiguration.setSelectedConfigurationId(newConfigurationId);
        }

    }

    @Nullable
    protected String getSavedSelectedConfigurationId() {
        return PhpInsightsProjectConfiguration.getInstance(this.myProject).getSelectedConfigurationId();
    }

    @Nullable
    protected String validate(@Nullable String configuration) {
        try {
            PhpInsightsProjectConfiguration.getInstance(this.myProject).findConfiguration(this.myProject, configuration);
            return null;
        } catch (QualityToolValidationException var3) {
            return var3.getMessage();
        }
    }

    @NotNull
    protected QualityToolConfigurationComboBox createConfigurationComboBox() {
        return new PhpInsightsConfigurationComboBox(this.myProject);
    }

    protected QualityToolsIgnoreFilesConfigurable getIgnoredFilesConfigurable() {
        return new PhpInsightsIgnoredFilesConfigurable(this.myProject);
    }
}
