package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionListener;
import java.util.List;

public class PhpInsightsConfigurationComboBox extends QualityToolConfigurationComboBox<PhpInsightsConfiguration> {
    public PhpInsightsConfigurationComboBox(@Nullable Project project) {
        super(project);
    }

    @NotNull
    protected ActionListener createBrowserAction(@NotNull Project project) {
        return (e) -> {
            QualityToolConfigurationItem item = this.getSelectedItem();
            PhpInsightsConfigurableList configurableList = new PhpInsightsConfigurableList(project, item == null ? null : item.getName());
            this.editConfigurableList(configurableList, item);
        };
    }

    @NotNull
    protected List<PhpInsightsConfiguration> getItems() {
        return PhpInsightsConfigurationManager.getInstance(this.myProject).getAllSettings();
    }

    @NotNull
    protected PhpInsightsConfiguration getDefaultItem() {
        return PhpInsightsConfigurationManager.getInstance(this.myProject).getLocalSettings();
    }
}
