package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;

public class AnnotatorInfo {
    private String tempFile;
    private final VirtualFile originalFile;
    private final PsiFile psiFile;
    @NotNull
    private final PhpInsightsInspection inspection;
    @NotNull
    private final Project project;
    private boolean isOnTheFly;
    private final String toolPath;
    private final int timeout;

    private AnnotatorInfo(@NotNull PsiFile psiFile, @NotNull PhpInsightsInspection inspection, @NotNull Project project, @NotNull String toolPath, int timeout, boolean isOnTheFly) {
        super();
        this.isOnTheFly = true;
        this.inspection = inspection;
        this.project = project;
        this.psiFile = psiFile;
        this.originalFile = psiFile.getVirtualFile();
        this.toolPath = toolPath;
        this.timeout = timeout;
        this.isOnTheFly = isOnTheFly;
    }

    public AnnotatorInfo(@NotNull PsiFile psiFile, @NotNull PhpInsightsInspection inspection, @NotNull Project project, @NotNull PhpInsightsConfiguration configuration, boolean isOnTheFly) {
        this(psiFile, inspection, project, configuration.getToolPath(), configuration.getTimeout(), isOnTheFly);
    }

    public String getFile() {
        return this.tempFile;
    }

    public String getFilePath() {
        return PathUtil.toSystemIndependentName(this.tempFile);
    }

    public PsiFile getPsiFile() {
        return this.psiFile;
    }

    @NotNull
    public PhpInsightsInspection getInspection() {
        return this.inspection;
    }

    @NotNull
    public Project getProject() {
        return this.project;
    }

    @NotNull
    public String getToolPath() {
        return this.toolPath;
    }

    public VirtualFile getOriginalFile() {
        return this.originalFile;
    }

    public boolean isOnTheFly() {
        return this.isOnTheFly;
    }

    public void setTempFile(@NotNull String tempFile) {
        this.tempFile = tempFile;
    }

    public int getTimeout() {
        return this.timeout;
    }
}
