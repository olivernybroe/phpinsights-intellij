package com.phpinsights.phpinsights;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBCheckBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

/**
 * This class is used for the preferences in inspection window.
 */
class PhpInsightsOptionsPanel {
    private JBCheckBox showInsightNameCheckBox;
    private TextFieldWithBrowseButton browseButton;
    private JPanel panel;

    PhpInsightsOptionsPanel(PhpInsightsInspection inspection) {
        // Add the show insight name button.
        this.showInsightNameCheckBox.setSelected(inspection.SHOW_INSIGHT_NAMES);
        this.showInsightNameCheckBox.addActionListener(
            e -> inspection.SHOW_INSIGHT_NAMES = this.showInsightNameCheckBox.isSelected()
        );

        // Add the config path browse button.
        browseButton.setText(inspection.CONFIG_PATH);
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
                inspection.CONFIG_PATH = browseButton.getText();
            }
        });

    }

    public JComponent getContentPane() {
        return panel;
    }
}
