package com.phpinsights.phpinsights;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PhpInsightsConfigurationManager extends QualityToolConfigurationManager<PhpInsightsConfiguration> {

    public PhpInsightsConfigurationManager(@Nullable Project project) {
        if (project != null) {
            this.myProjectManager = ServiceManager.getService(project, PHPInsightsProjectConfigurationManager.class);
        }

        this.myApplicationManager = ServiceManager.getService(PhpInsightsAppConfigurationManager.class);
    }

    @NotNull
    protected List<PhpInsightsConfiguration> getDefaultProjectSettings() {
        return ServiceManager.getService(
            ProjectManager.getInstance().getDefaultProject(),
            PHPInsightsProjectConfigurationManager.class
        ).getSettings();
    }

    public static PhpInsightsConfigurationManager getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, PhpInsightsConfigurationManager.class);
    }

    @State(
        name = "PhpInsights",
        storages = {@Storage("php.xml")}
    )
    static class PhpInsightsAppConfigurationManager extends PhpInsightsConfigurationBaseManager {
        PhpInsightsAppConfigurationManager() {
        }
    }

    @State(
        name = "PhpInsights",
        storages = {@Storage("php.xml")}
    )
    static class PHPInsightsProjectConfigurationManager extends PhpInsightsConfigurationBaseManager {
        PHPInsightsProjectConfigurationManager() {
        }
    }
}
