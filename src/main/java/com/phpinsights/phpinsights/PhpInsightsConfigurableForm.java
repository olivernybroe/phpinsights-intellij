package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsConfigurableForm<C extends PhpInsightsConfiguration> extends QualityToolConfigurableForm<C> {
    PhpInsightsConfigurableForm(@NotNull Project project, @NotNull C configuration) {
        super(
            project,
            configuration,
            PhpInsights.TOOL_NAME.toString(),
            PhpInsights.LAUNCHER_NAME.toString()
        );
    }

    @Nls
    public String getDisplayName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    @Nullable
    public String getHelpTopic() {
        return PhpInsights.HELP_TOPIC.toString();
    }

    @NotNull
    public String getId() {
        return PhpInsightsConfigurableForm.class.getName();
    }

    @NotNull
    public Pair<Boolean, String> validateMessage(String message) {
        return message.startsWith("PHP Insights") ? Pair.create(true, "OK, " + message) : Pair.create(false, message);
    }

    public boolean isValidToolFile(VirtualFile file) {
        return file.getName().startsWith("phpinsights");
    }

}
