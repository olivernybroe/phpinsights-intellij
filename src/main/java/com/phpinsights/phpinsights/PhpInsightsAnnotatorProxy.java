package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.intellij.util.containers.ContainerUtil.*;
import static java.util.Collections.singletonList;

public class PhpInsightsAnnotatorProxy extends QualityToolAnnotator<PhpInsightsValidationInspection> {
    public final static PhpInsightsAnnotatorProxy INSTANCE = new PhpInsightsAnnotatorProxy();

    @Override
    @Nullable
    protected List<String> getOptions(@Nullable String filePath, @NotNull PhpInsightsValidationInspection inspection, @NotNull Project project) {
        return emptyList();
    }

    @Override
    protected @Nullable List<String> getOptions(@Nullable String filePath,
                                                @NotNull PhpInsightsValidationInspection inspection,
                                                @NotNull Project project,
                                                boolean isOnTheFly) {
        final PhpInsightsGlobalInspection tool = (PhpInsightsGlobalInspection)getQualityToolType().getGlobalTool((project));
        if (tool == null) {
            return emptyList();
        }

        if (isOnTheFly) {
            return tool.getCommandLineOptions(singletonList(filePath), project);
        }

        if (isNotEmpty(tool.config)) {
            return tool.getCommandLineOptions(emptyList(), project);
        }

        return tool.getCommandLineOptions(
                concat(
                        singletonList(filePath),
                        map(
                                ProjectRootManager.getInstance(project).getContentSourceRoots(),
                                VirtualFile::getPath
                        )
                ),
                project
        );
    }

    @Override
    protected QualityToolMessageProcessor createMessageProcessor(@NotNull QualityToolAnnotatorInfo collectedInfo) {
        return new MessageProcessor(collectedInfo);
    }

    @NotNull
    @Override
    protected QualityToolAnnotatorInfo<PhpInsightsValidationInspection> createAnnotatorInfo(@Nullable PsiFile file,
                                                                                        PhpInsightsValidationInspection tool,
                                                                                        Project project,
                                                                                        QualityToolConfiguration configuration,
                                                                                        boolean isOnTheFly) {
        return new PhpInsightsQualityToolAnnotatorInfo(file, tool, project, configuration, isOnTheFly);
    }

    @Override
    protected void addAdditionalAnnotatorInfo(QualityToolAnnotatorInfo collectedInfo, QualityToolValidationInspection tool) {
    }

    @Override
    protected @NotNull QualityToolType getQualityToolType() {
        return PhpInsightsQualityToolType.INSTANCE;
    }
}