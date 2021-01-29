package com.phpinsights.phpinsights;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBCheckBox;
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse;
import com.jetbrains.php.tools.quality.QualityToolCommonConfigurable;
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import static com.jetbrains.php.lang.inspections.PhpInspectionsUtil.createPanelWithSettingsLink;

/**
 * This class is used for the preferences in inspection window.
 */
class PhpInsightsOptionsPanel extends QualityToolsOptionsPanel {
    private PhpTextFieldWithSdkBasedBrowse browseButton;
    private JPanel panel;
    private JPanel myLinkPanel;

    PhpInsightsOptionsPanel(PhpInsightsGlobalInspection inspection) {
        // Add the config path browse button.
        browseButton.setText(inspection.config);
        browseButton.addBrowseFolderListener(
            new TextBrowseFolderListener(
                new FileChooserDescriptor(
                    true,
                    false,
                    false,
                    false,
                    false,
                    false
                )
            )
        );
        browseButton.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
            protected void textChanged(@NotNull DocumentEvent e) {
                inspection.config = browseButton.getText();
            }
        });

    }

    private void createUIComponents() {
        myLinkPanel = createPanelWithSettingsLink(PhpInsights.TOOL_NAME.toString(),
                QualityToolCommonConfigurable.class,
                QualityToolCommonConfigurable::new,
                i -> i.showConfigurable(PhpInsights.TOOL_NAME.toString()));
    }

    @Override
    public JPanel getOptionsPanel() {
        return panel;
    }

    public void init() {
    }
}
