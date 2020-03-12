/****************************************************************************
 * Copyright 2020, Optimizely, Inc. and contributors                        *
 *                                                                          *
 * Licensed under the Apache License, Version 2.0 (the "License");          *
 * you may not use this file except in compliance with the License.         *
 * You may obtain a copy of the License at                                  *
 *                                                                          *
 *    http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                          *
 * Unless required by applicable law or agreed to in writing, software      *
 * distributed under the License is distributed on an "AS IS" BASIS,        *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
 * See the License for the specific language governing permissions and      *
 * limitations under the License.                                           *
 ***************************************************************************/
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

        Optimizely optimizely = service.getBySDKKey(txt);

        if (optimizely == null || !optimizely.isValid()) {
            Messages.showErrorDialog("Optimizely did not intialize correctly with sdk key " + txt, "Problem with SDK Key");
        }
        else {
            Messages.showInfoMessage("Optimizely intialized correctly", "Use Optimizely");
        }

    }
}
