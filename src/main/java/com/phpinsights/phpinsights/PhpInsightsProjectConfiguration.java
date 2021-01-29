package com.phpinsights.phpinsights;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import com.jetbrains.php.tools.quality.QualityToolProjectConfiguration;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PhpInsightsProjectConfiguration", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class PhpInsightsProjectConfiguration extends QualityToolProjectConfiguration<PhpInsightsConfiguration>
    implements PersistentStateComponent<PhpInsightsProjectConfiguration> {


    @Override
    public @Nullable PhpInsightsProjectConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PhpInsightsProjectConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    protected QualityToolType<PhpInsightsConfiguration> getQualityToolType() {
        return PhpInsightsQualityToolType.INSTANCE;
    }

    public static PhpInsightsProjectConfiguration getInstance(Project project) {
        return ServiceManager.getService(project, PhpInsightsProjectConfiguration.class);
    }
}
