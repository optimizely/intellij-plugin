package com.optimizely.intellij.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.optimizely.ab.Optimizely;
import com.optimizely.intellij.plugin.components.OptDialogWrapper;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import org.jetbrains.annotations.NotNull;

public class DebugAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OptimizelyFactoryService factory = ServiceManager.getService(OptimizelyFactoryService.class);

        Optimizely optimizely = factory.getCurrentOptimizely();
        if (optimizely == null) {
            Messages.showErrorDialog("Please set your SDK Key first!", "Optimizely not initialized");
            return;
        }

        // Using the event, create and show a dialog
        Project currentProject = e.getProject();

        if (currentProject == null) return;

        if(new OptDialogWrapper().showAndGet()) {
            // user pressed ok
        }

    }
}
