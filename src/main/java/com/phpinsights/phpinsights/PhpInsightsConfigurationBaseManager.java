package com.phpinsights.phpinsights;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.php.tools.quality.QualityToolConfigurationBaseManager;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsConfigurationBaseManager extends QualityToolConfigurationBaseManager<PhpInsightsConfiguration> {
    private static final @NlsSafe  String PHP_INSIGHTS_PATH = "PhpInsightsPath";
    private static final @NlsSafe String ROOT_NAME = "PhpInsights_settings";

    @Override
    protected @NotNull QualityToolType<PhpInsightsConfiguration> getQualityToolType() {
        return PhpInsightsQualityToolType.INSTANCE;
    }

    @NotNull
    @Override
    protected String getOldStyleToolPathName() {
        return PHP_INSIGHTS_PATH;
    }

    @NotNull
    @Override
    protected String getConfigurationRootName() {
        return ROOT_NAME;
    }

    @Override
    @Nullable
    protected PhpInsightsConfiguration loadLocal(Element element) {
        return XmlSerializer.deserialize(element, PhpInsightsConfiguration.class);
    }

    public static PhpInsightsConfigurationBaseManager getInstance() {
        return ApplicationManager.getApplication().getService(PhpInsightsConfigurationBaseManager.class);
    }
}
