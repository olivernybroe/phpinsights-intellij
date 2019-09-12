package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.intellij.util.ObjectUtils;
import com.jetbrains.php.tools.quality.QualityToolConfigurableList;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsConfigurableList extends QualityToolConfigurableList<PhpInsightsConfiguration> {
    public static final String HELP_TOPIC = "reference.settings.php.phpinsights";

    @NonNls
    private static final String SUBJ_DISPLAY_NAME = "phpinsights";

    public PhpInsightsConfigurableList(@NotNull Project project, @Nullable String initialElement) {
        super(
            project,
            PhpInsightsConfigurationManager.getInstance(project),
            PhpInsightsConfiguration::new,
            PhpInsightsConfiguration::clone,
            (settings) -> new PhpInsightsConfigurableForm<>(project, settings),
            initialElement
        );
        this.setSubjectDisplayName(SUBJ_DISPLAY_NAME);
    }

    @Nullable
    protected QualityToolConfigurationProvider<PhpInsightsConfiguration> getConfigurationProvider() {
        return null;
    }

    @Nullable
    protected PhpInsightsConfiguration getConfiguration(@Nullable QualityToolConfiguration configuration) {
        return ObjectUtils.tryCast(configuration, PhpInsightsConfiguration.class);
    }

    @Nls
    public String getDisplayName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    public String getHelpTopic() {
        return HELP_TOPIC;
    }

}
