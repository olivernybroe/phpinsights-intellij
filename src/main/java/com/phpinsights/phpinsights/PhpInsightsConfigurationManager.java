package com.phpinsights.phpinsights;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsConfigurationManager extends QualityToolConfigurationManager<PhpInsightsConfiguration> {
    public static final int DEFAULT_MAX_MESSAGES_PER_FILE = 50;

    protected PhpInsightsConfigurationManager(@Nullable Project project) {
        super(project);

        if (project != null) {
            myProjectManager = ServiceManager.getService(project, PhpInsightsProjectConfigurationManager.class);
        }
        myApplicationManager = ApplicationManager.getApplication().getService(PhpInsightsAppConfigurationManager.class);
    }

    public static PhpInsightsConfigurationManager getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, PhpInsightsConfigurationManager.class);
    }

    @State(name = "PhpStan", storages = @Storage("php.xml"))
    static class PhpInsightsProjectConfigurationManager extends PhpInsightsConfigurationBaseManager {
    }

    @State(name = "PhpStan", storages = @Storage("php.xml"))
    static class PhpInsightsAppConfigurationManager extends PhpInsightsConfigurationBaseManager {
    }
}
