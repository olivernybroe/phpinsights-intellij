package com.phpinsights.phpinsights;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.*;
import com.intellij.util.ui.GridBag;
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
    private TextFieldWithBrowseButton browseButton;

    PhpInsightsOptionsPanel(PhpInsightsInspection inspection) {
        // Add the show insight name button.
        this.showInsightNameCheckBox = new JBCheckBox("Show insight names");
        this.showInsightNameCheckBox.setSelected(inspection.SHOW_INSIGHT_NAMES);
        this.showInsightNameCheckBox.addActionListener(
            e -> inspection.SHOW_INSIGHT_NAMES = this.showInsightNameCheckBox.isSelected()
        );

        // Add the config path browse button.
        browseButton = new TextFieldWithBrowseButton();
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

    JPanel getContentPane() {
        JBPanel panel = new JBPanel(new VerticalFlowLayout());
        panel.add(this.showInsightNameCheckBox);


        panel.add(new JBLabel("Config file:"));
        panel.add(browseButton);

        return panel;
    }

}
