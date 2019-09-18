package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Transient;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is responsible for storing all the settings used in the plugin.
 */
public class PhpInsightsConfiguration implements QualityToolConfiguration {
    public static final String LOCAL = "Local";
    private String myPhpInsightsPath = "";
    private int myMaxMessagesPerFile = 50;
    private int myTimeoutMs = 5000;

    PhpInsightsConfiguration() {
    }

    @Transient
    public String getToolPath() {
        return this.myPhpInsightsPath;
    }

    public void setToolPath(String toolPath) {
        this.myPhpInsightsPath = toolPath;
    }

    @Attribute("tool_path")
    @Nullable
    public String getSerializedToolPath() {
        return this.serialize(this.myPhpInsightsPath);
    }

    public void setSerializedToolPath(@Nullable String configurationFilePath) {
        this.myPhpInsightsPath = this.deserialize(configurationFilePath);
    }

    @Attribute("max_messages_per_file")
    public int getMaxMessagesPerFile() {
        return this.myMaxMessagesPerFile;
    }

    public void setMaxMessagesPerFile(int maxMessagesPerFile) {
        this.myMaxMessagesPerFile = maxMessagesPerFile;
    }

    @Attribute("timeout")
    public int getTimeout() {
        return this.myTimeoutMs;
    }

    public void setTimeout(int timeout) {
        this.myTimeoutMs = timeout;
    }

    public String getPresentableName(@Nullable Project project) {
        return this.getId();
    }

    public String getId() {
        return LOCAL;
    }

    public String getInterpreterId() {
        return null;
    }

    @NotNull
    public PhpInsightsConfiguration clone() {
        PhpInsightsConfiguration settings = new PhpInsightsConfiguration();
        this.clone(settings);
        return settings;
    }

    public PhpInsightsConfiguration clone(@NotNull PhpInsightsConfiguration settings) {
        settings.myPhpInsightsPath = this.myPhpInsightsPath;
        settings.myMaxMessagesPerFile = this.myMaxMessagesPerFile;
        settings.myTimeoutMs = this.myTimeoutMs;
        return settings;
    }

    public int compareTo(@NotNull QualityToolConfiguration o) {
        if (!(o instanceof PhpInsightsConfiguration)) {
            return 1;
        } else if (StringUtil.equals(this.getPresentableName(null), LOCAL)) {
            return -1;
        } else {
            return StringUtil.equals(o.getPresentableName(null), LOCAL)
                ? 1
                : StringUtil.compare(this.getPresentableName(null), o.getPresentableName(null), false);
        }
    }
}
