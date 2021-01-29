package com.phpinsights.phpinsights;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolValidationGlobalInspection;
import com.jetbrains.php.tools.quality.QualityToolXmlMessageProcessor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.jetbrains.php.tools.quality.QualityToolAnnotator.updateIfRemote;

public class PhpInsightsGlobalInspection extends QualityToolValidationGlobalInspection {
    public static final Key<List<QualityToolXmlMessageProcessor.ProblemDescription>> PHP_INSIGHTS_ANNOTATOR_INFO = Key.create("ANNOTATOR_INFO_3");
    public @NlsSafe String config = "";

    @NonNls private static final String START_COMMAND = "analyse";
    @NonNls private static final String CHECKSTYLE_FORMAT_PARAMETER = "--format=checkstyle";
    @NonNls private static final String CONFIG_PATH_PARAMETER = "--config-path=%s";

    @Override
    public JComponent createOptionsPanel() {
        final PhpInsightsOptionsPanel optionsPanel = new PhpInsightsOptionsPanel(this);
        optionsPanel.init();
        return optionsPanel.getOptionsPanel();
    }

    @Override
    public @Nullable LocalInspectionTool getSharedLocalInspectionTool() {
        return new PhpInsightsValidationInspection();
    }

    @Override
    protected @NotNull QualityToolAnnotator getAnnotator() {
        return PhpInsightsAnnotatorProxy.INSTANCE;
    }

    @Override
    protected Key<List<QualityToolXmlMessageProcessor.ProblemDescription>> getKey() {
        return PHP_INSIGHTS_ANNOTATOR_INFO;
    }

    public List<String> getCommandLineOptions(@NotNull List<String> filePath, @NotNull Project project) {
        @NonNls ArrayList<String> options = new ArrayList<>();
        options.add(START_COMMAND);

        final List<String> filePaths = ContainerUtil.filter(filePath, Objects::nonNull);
        options.addAll(filePaths);

        if (isNotEmpty(config)) {
            options.add(
                String.format(
                    CONFIG_PATH_PARAMETER,
                    updateIfRemote(config, project, PhpInsightsQualityToolType.INSTANCE)
                )
            );
        }

        options.add(CHECKSTYLE_FORMAT_PARAMETER);

        return options;
    }
}