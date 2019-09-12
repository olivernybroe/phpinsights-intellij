package com.phpinsights.phpinsights;

import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.Field;

public class PhpInsightsInspection extends QualityToolValidationInspection {
    public boolean SHOW_INSIGHT_NAMES;

    @Nullable
    public String CONFIG_PATH = null;

    public PhpInsightsInspection() {
        this.SHOW_INSIGHT_NAMES = false;
    }

    @NotNull
    public String[] getGroupPath() {
        return PhpInspection.GROUP_PATH_GENERAL;
    }

    @NotNull
    public String getShortName() {
        return this.getClass().getSimpleName();
    }

    @Nls
    @NotNull
    public String getDisplayName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    @Nullable
    public JComponent createOptionsPanel() {
        return (new PhpInsightsOptionsPanel(this)).getContentPane();
    }

    @NotNull
    protected QualityToolAnnotator getAnnotator() {
        return PhpInsightsAnnotator.INSTANCE;
    }

    public String getToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }

}
