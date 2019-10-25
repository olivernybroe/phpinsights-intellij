package com.phpinsights.phpinsights;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Transient;
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
public class PhpInsightsConfiguration implements PersistentStateComponent<PhpInsightsConfiguration> {
    @NonNls public static final String STATE_NAME = "PhpInsightsConfiguration";
    @NonNls public static final String STATE_STORAGE = "$WORKSPACE_FILE$";
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

    @NonNls
    @Attribute("tool_path")
    @Nullable
    public String getSerializedToolPath() {
        return FileUtil.toSystemIndependentName(this.phpInsightPath);
    }

    public void setSerializedToolPath(@Nullable String configurationFilePath) {
        this.phpInsightPath = configurationFilePath != null
            ? FileUtil.toSystemDependentName(configurationFilePath)
            : null;
    }

    @NonNls
    @Attribute("timeout")
    public int getTimeout() {
        return this.timeoutMs;
    }

    public void setTimeout(int timeout) {
        this.timeoutMs = timeout;
    }


    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    @Nullable
    @Override
    public PhpInsightsConfiguration getState() {
        return this;
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * config files were externally changed while IDE was running.
     * <p>
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see XmlSerializerUtil#copyBean(Object, Object)
     */
    @Override
    public void loadState(@NotNull PhpInsightsConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
