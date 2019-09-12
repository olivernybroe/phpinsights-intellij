package com.phpinsights.phpinsights;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jetbrains.php.tools.quality.QualityToolProjectConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "PhpInsightsProjectConfiguration",
    storages = {@Storage("$WORKSPACE_FILE$")}
)
public class PhpInsightsProjectConfiguration extends QualityToolProjectConfiguration<PhpInsightsConfiguration> implements PersistentStateComponent<PhpInsightsProjectConfiguration> {
    public PhpInsightsProjectConfiguration() {
    }

    public static PhpInsightsProjectConfiguration getInstance(Project project) {
        return ServiceManager.getService(project, PhpInsightsProjectConfiguration.class);
    }

    public String getInspectionId() {
        return new PhpInsightsInspection().getID();
    }

    @Nullable
    public PhpInsightsProjectConfiguration getState() {
        return this;
    }

    public void loadState(@NotNull PhpInsightsProjectConfiguration state) {

        XmlSerializerUtil.copyBean(state, this);
    }

    @NotNull
    protected String getQualityToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }

    @NotNull
    protected PhpInsightsConfigurationManager getConfigurationManager(@NotNull Project project) {
        return PhpInsightsConfigurationManager.getInstance(project);
    }
}