package com.phpinsights.phpinsights;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpInsightsQualityToolAnnotatorInfo extends QualityToolAnnotatorInfo<PhpInsightsValidationInspection> {
    public PhpInsightsQualityToolAnnotatorInfo(@Nullable PsiFile psiFile,
                                           @NotNull PhpInsightsValidationInspection inspection,
                                           @NotNull Project project,
                                           @NotNull QualityToolConfiguration configuration, boolean isOnTheFly) {
        super(psiFile, inspection, project, configuration, isOnTheFly);
    }
}