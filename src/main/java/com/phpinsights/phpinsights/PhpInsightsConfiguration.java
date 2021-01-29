package com.phpinsights.phpinsights;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Transient;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is responsible for storing all the settings used in the plugin.
 */
@State(
    name = PhpInsightsConfiguration.STATE_NAME,
    storages = {@Storage(PhpInsightsConfiguration.STATE_STORAGE)}
)
public class PhpInsightsConfiguration implements QualityToolConfiguration {
    @NonNls public static final String STATE_NAME = "PhpInsightsConfiguration";
    @NonNls public static final String STATE_STORAGE = "$WORKSPACE_FILE$";
    private int maxMessagesPerFile = PhpInsightsConfigurationManager.DEFAULT_MAX_MESSAGES_PER_FILE;
    private String phpInsightPath = "";
    private int timeoutMs = 5000;

    PhpInsightsConfiguration() {
    }

    public static PhpInsightsConfiguration getInstance(Project project)
    {
        return ServiceManager.getService(project, PhpInsightsConfiguration.class);
    }

    @Transient
    public String getToolPath() {
        return this.phpInsightPath;
    }

    public void setToolPath(String toolPath) {
        this.phpInsightPath = toolPath;
    }

    @Override
    public int getMaxMessagesPerFile() {
        return maxMessagesPerFile;
    }

    @Override
    public QualityToolConfiguration clone() {
        PhpInsightsConfiguration settings = new PhpInsightsConfiguration();
        clone(settings);
        return settings;
    }

    public PhpInsightsConfiguration clone(@NotNull PhpInsightsConfiguration settings) {
        settings.phpInsightPath = phpInsightPath;
        settings.maxMessagesPerFile = maxMessagesPerFile;
        settings.timeoutMs = timeoutMs;
        return settings;
    }

    @NonNls
    @Attribute("tool_path")
    @Nullable
    public String getSerializedToolPath() {
        return serialize(this.phpInsightPath);
    }

    public void setSerializedToolPath(@Nullable String configurationFilePath) {
        this.phpInsightPath = deserialize(configurationFilePath);
    }

    @Override
    public @Nls String getId() {
        return PhpInsightsBundle.message("LOCAL");
    }

    @Override
    @NotNull
    public @NlsContexts.Label String getPresentableName(@Nullable Project project) {
        return getId();
    }

    @Override
    public String getInterpreterId() {
        return null;
    }

    @NonNls
    @Attribute("timeout")
    public int getTimeout() {
        return this.timeoutMs;
    }

    public void setTimeout(int timeout) {
        this.timeoutMs = timeout;
    }

    @Override
    public int compareTo(@NotNull QualityToolConfiguration o) {
        if (!(o instanceof PhpInsightsConfiguration)) {
            return 1;
        }

        if (StringUtil.equals(getPresentableName(null), PhpInsightsBundle.message("LOCAL"))) {
            return -1;
        }
        else if (StringUtil.equals(o.getPresentableName(null), PhpInsightsBundle.message("LOCAL"))) {
            return 1;
        }
        return StringUtil.compare(getPresentableName(null), o.getPresentableName(null), false);
    }
}
