package com.phpinsights.phpinsights;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.IntegerField;
import com.intellij.util.ui.UIUtil;
import com.jetbrains.php.ui.PhpUiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class PhpInsightsConfigurable implements SearchableConfigurable, Configurable.NoScroll {
    @NonNls
    public static final String TOOL_FILENAME = "phpinsights";
    @NonNls
    public static final String VERSION_PARAMETER = "--version";
    @NonNls
    public static final String VALIDATE_MESSAGE_PREPEND_TEXT = "PHP Insights";
    private final PhpInsightsConfiguration configuration;

    private JPanel panel;
    private TextFieldWithBrowseButton toolPathField;
    private JButton validateButton;
    private JBLabel errorLabel;
    private IntegerField timeoutField;

    public PhpInsightsConfigurable(PhpInsightsConfiguration configuration) {
        this.configuration = configuration;
        
        this.validateButton.addActionListener((e -> this.validateConfiguration()));
        addBrowseFolderListener();
    }

    private void validateConfiguration() {
        Pair<Boolean, String> response = this.validateConfiguration(this.toolPathField.getText());
        this.updateValidationLabel(response);
    }

    private void updateValidationLabel(@NotNull Pair<Boolean, String> info) {
        FontMetrics fontMetrics = this.errorLabel.getFontMetrics(this.errorLabel.getFont());
        String[] text = UIUtil.splitText(info.second, fontMetrics, this.panel.getWidth() > 100 ? this.panel.getWidth() - 100 : 700, ' ');
        this.errorLabel.setText(PhpUiUtil.surroundWithHtml(StringUtil.join(text, "<br>")));
        this.errorLabel.setIcon(info.first ? UIUtil.getBalloonInformationIcon() : UIUtil.getBalloonErrorIcon());
        this.errorLabel.setVisible(true);
    }

    @NotNull
    private Pair<Boolean, String> validateConfiguration(String toolPath) {
        String path = this.toolPathField.getText();
        if (StringUtil.isEmptyOrSpaces(path)) {
            return Pair.create(false, PhpInsightsBundle.message("VALIDATE_TOOL_PATH_NO_PATH"));
        } else {
            try {
                ProcessOutput output =  ProcessCreator.getToolOutput(
                    toolPath,
                    configuration.getTimeout(),
                    VERSION_PARAMETER
                );
                return this.validateToolResult(output.getStdout());
            } catch (ExecutionException e) {
                return Pair.create(false, e.getMessage());
            }
        }
    }

    @NotNull
    public Pair<Boolean, String> validateToolResult(@Nullable String message) {
        return StringUtil.isEmpty(message)
            ? new Pair<>(false, PhpInsightsBundle.message("FAILED_RUNNING_PHP_INSIGHTS"))
            : this.validateMessage(message);
    }

    @NotNull
    public Pair<Boolean, String> validateMessage(String message) {
        return message.startsWith(VALIDATE_MESSAGE_PREPEND_TEXT)
            ? Pair.create(true, PhpInsightsBundle.message("VALID_TOOL_PATH", message))
            : Pair.create(false, message);
    }
    
    protected void addBrowseFolderListener() {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false) {
            public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
                return file.isDirectory() || isValidToolFile(file);
            }
        };
        toolPathField.addBrowseFolderListener(
            PhpInsightsBundle.message("TOOL_PATH_BROWSE_TITLE"),
            PhpInsightsBundle.message("TOOL_PATH_BROWSE_DESCRIPTION"),
            null,
            descriptor
        );
    }

    public boolean isValidToolFile(VirtualFile file) {
        return file.getName().startsWith(TOOL_FILENAME);
    }

    /**
     * Unique configurable id.
     * Note this id should be THE SAME as the one specified in XML.
     */
    @NotNull
    @Override
    public String getId() {
        return PhpInsightsConfigurable.class.getName();
    }

    /**
     * Returns the visible name of the configurable component.
     * Note, that this method must return the display name
     * that is equal to the display name declared in XML
     * to avoid unexpected errors.
     *
     * @return the visible name of the configurable component
     */
    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return PhpInsights.DISPLAY_NAME.toString();
    }

    /**
     * Creates new Swing form that enables user to configure the settings.
     * Usually this method is called on the EDT, so it should not take a long time.
     * <p>
     * Also this place is designed to allocate resources (subscriptions/listeners etc.)
     *
     * @return new Swing form to show, or {@code null} if it cannot be created
     * @see #disposeUIResources
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        return this.panel;
    }

    /**
     * Indicates whether the Swing form was modified or not.
     * This method is called very often, so it should not take a long time.
     *
     * @return {@code true} if the settings were modified, {@code false} otherwise
     */
    @Override
    public boolean isModified() {
        return !this.configuration.getToolPath().equals(this.toolPathField.getText())
                || this.configuration.getTimeout() != this.timeoutField.getValue();
    }

    /**
     * Stores the settings from the Swing form to the configurable component.
     * This method is called on EDT upon user's request.
     *
     */
    @Override
    public void apply() {
        this.configuration.setToolPath(this.toolPathField.getText());
        this.configuration.setTimeout(this.timeoutField.getValue());
    }

    public void reset() {
        this.toolPathField.setText(this.configuration.getToolPath());
        this.timeoutField.setValue(this.configuration.getTimeout());
    }
}