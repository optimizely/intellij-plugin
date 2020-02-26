package com.optimizely.intellij.plugin.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.optimizely.ab.Optimizely;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import icons.ActionBasicsIcons;
import org.jetbrains.annotations.NotNull;

public class SetAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        String txt= Messages.showInputDialog(project, "What is your SDK Key?",
                "Input your SDKKey", ActionBasicsIcons.Sdk_default_icon);

        if (txt == null) {
            return;
        }
        
        OptimizelyFactoryService service = ServiceManager.getService(OptimizelyFactoryService.class);

        service.getBySDKKey(txt);

//        if (optimizely.isValid()) {
//            Messages.showErrorDialog("Optimizely did not intialize correctly with sdk key " + txt, "Problem with SDK Key");
//        }
//        else {
//            Messages.showInfoMessage("Optimizely intialized correctly", "Use Optimizely");
//        }

    }
}
