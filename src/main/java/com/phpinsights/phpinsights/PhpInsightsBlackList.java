package com.phpinsights.phpinsights;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolBlackList;
import org.jetbrains.annotations.NonNls;

@State(
    name = PhpInsightsBlackList.PHP_INSIGHTS_BLACK_LIST,
    storages = @Storage(StoragePathMacros.WORKSPACE_FILE)
)
public class PhpInsightsBlackList extends QualityToolBlackList {
    @NonNls public static final String PHP_INSIGHTS_BLACK_LIST = "PhpInsightsBlackList";

    public static PhpInsightsBlackList getInstance(Project project) {
        return ServiceManager.getService(project, PhpInsightsBlackList.class);
    }
}
