package com.phpinsights.phpinsights;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolBlackList;

@State(
    name = "PhpInsightsBlackList",
    storages = {@Storage("$WORKSPACE_FILE$")}
)
public class PhpInsightsBlackList extends QualityToolBlackList {
    public static PhpInsightsBlackList getInstance(Project project) {
        return ServiceManager.getService(project, PhpInsightsBlackList.class);
    }
}
