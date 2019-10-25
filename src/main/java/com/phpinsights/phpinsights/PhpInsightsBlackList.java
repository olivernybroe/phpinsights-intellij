package com.phpinsights.phpinsights;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolBlackList;
import org.jetbrains.annotations.NonNls;

@State(
    name = PhpInsightsBlackList.PHP_INSIGHTS_BLACK_LIST,
    storages = {@Storage(PhpInsightsBlackList.BLACK_LIST_STORAGE)}
)
public class PhpInsightsBlackList extends QualityToolBlackList {
    @NonNls public static final String PHP_INSIGHTS_BLACK_LIST = "PhpInsightsBlackList";
    @NonNls public static final String BLACK_LIST_STORAGE = "$WORKSPACE_FILE$";

    public static PhpInsightsBlackList getInstance(Project project) {
        return ServiceManager.getService(project, PhpInsightsBlackList.class);
    }
}
