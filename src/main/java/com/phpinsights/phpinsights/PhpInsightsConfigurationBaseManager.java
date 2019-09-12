package com.phpinsights.phpinsights;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.php.tools.quality.QualityToolConfigurationBaseManager;
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsConfigurationBaseManager extends QualityToolConfigurationBaseManager<PhpInsightsConfiguration> {
    private static final String MESS_DETECTOR_PATH = "phpInsightsPath";
    private static final String ROOT_NAME = "phpinsights_settings";

    public static PhpInsightsConfigurationBaseManager getInstance() {
        return ServiceManager.getService(PhpInsightsConfigurationBaseManager.class);
    }

    @NotNull
    protected PhpInsightsConfiguration createLocalSettings() {
        return new PhpInsightsConfiguration();
    }

    @NotNull
    protected String getQualityToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }

    @NotNull
    protected String getOldStyleToolPathName() {
        return MESS_DETECTOR_PATH;
    }

    @NotNull
    protected String getConfigurationRootName() {
        return ROOT_NAME;
    }

    @Nullable
    protected QualityToolConfigurationProvider<PhpInsightsConfiguration> getConfigurationProvider() {
        return null;
    }

    @Nullable
    protected PhpInsightsConfiguration loadLocal(Element element) {
        return XmlSerializer.deserialize(element, PhpInsightsConfiguration.class);
    }
}
