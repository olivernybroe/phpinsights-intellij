package com.phpinsights.phpinsights;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.tools.quality.QualityToolBlackList;
import com.jetbrains.php.tools.quality.QualityToolsIgnoreFilesConfigurable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhpInsightsAddToIgnoredAction extends DumbAwareAction {
    @NotNull
    protected QualityToolBlackList getBlackList(Project project) {
        return PhpInsightsBlackList.getInstance(project);
    }

    @NotNull
    protected String getToolName() {
        return PhpInsights.TOOL_NAME.toString();
    }

    protected QualityToolsIgnoreFilesConfigurable getIgnoredFilesConfigurable(@NotNull Project project) {
        return new PhpInsightsIgnoredFilesConfigurable(project);
    }

    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        DataContext dataContext = actionEvent.getDataContext();
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        Collection<VirtualFile> selectedFiles = this.getSelectedFiles(dataContext);
        if (!selectedFiles.isEmpty() && project != null) {
            QualityToolBlackList blackList = this.getBlackList(project);
            selectedFiles.forEach(blackList::addFile);
            this.showConfigurable(project, selectedFiles);
        }
    }

    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        if (this.getSelectedFiles(e.getDataContext()).size() == 0) {
            presentation.setVisible(false);
        } else {
            presentation.setVisible(true);
            presentation.setText(this.getToolName());
        }
    }

    @NotNull
    private Collection<VirtualFile> getSelectedFiles(@NotNull DataContext dataContext) {
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            return ContainerUtil.emptyList();
        } else {
            List<VirtualFile> filteredFiles = new ArrayList<>();
            VirtualFile[] selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
            QualityToolBlackList blackList = this.getBlackList(project);
            if (selectedFiles != null) {
                for (VirtualFile file : selectedFiles) {
                    if (!blackList.containsFile(file) && PhpFileType.INSTANCE.equals(file.getFileType())) {
                        filteredFiles.add(file);
                    }
                }
            }

            return filteredFiles;
        }
    }

    private void showConfigurable(@NotNull Project project, @NotNull Collection<VirtualFile> selectedFiles) {
        QualityToolsIgnoreFilesConfigurable ignoredFilesConfigurable = this.getIgnoredFilesConfigurable(project);
        ShowSettingsUtil.getInstance().editConfigurable(project, ignoredFilesConfigurable, () -> ignoredFilesConfigurable.selectFiles(selectedFiles));
    }
}
