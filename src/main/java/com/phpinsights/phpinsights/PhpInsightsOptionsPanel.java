package com.phpinsights.phpinsights;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBCheckBox;
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse;
import com.jetbrains.php.tools.quality.phpcs.PhpCSOptionsPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

/**
 * This class is used for the preferences in inspection window.
 */
class PhpInsightsOptionsPanel {
    private static final Logger LOG = Logger.getInstance(PhpInsightsOptionsPanel.class);
    private JBCheckBox showInsightNameCheckBox;
    private PhpInsightsInspection inspection;

    PhpInsightsOptionsPanel(PhpInsightsInspection inspection) {
        this.inspection = inspection;

        this.showInsightNameCheckBox = new JBCheckBox("Show insight names");
        this.showInsightNameCheckBox.setSelected(inspection.SHOW_INSIGHT_NAMES);
        this.showInsightNameCheckBox.addActionListener(
            e -> inspection.SHOW_INSIGHT_NAMES = this.showInsightNameCheckBox.isSelected()
        );

    }

    JPanel getContentPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(this.showInsightNameCheckBox);

        TextFieldWithBrowseButton browseButton = new TextFieldWithBrowseButton();
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
                PhpInsightsOptionsPanel.this.inspection.CONFIG_PATH = browseButton.getText();
            }
        });

        panel.add(browseButton);

        return panel;
    }

}
