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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.optimizely.ab.Optimizely;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JumpToAction extends AnAction {

    private boolean isFeature;
    private String key;

    public JumpToAction(@Nullable String text, @Nullable String description, @Nullable Icon icon, Boolean isFeature, String key) {
        super(text, description, icon);
        this.isFeature = isFeature;
        this.key = key;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String gotoFeature = "https://app.optimizely.com/v2/projects/%s/features/%s#modal";
        String gotoExperiment = "https://app.optimizely.com/v2/projects/%s/experiments/%s/variations";
        Optimizely optimizely = ServiceManager.getService(OptimizelyFactoryService.class).getCurrentOptimizely();
        String gotoAction;
        if (isFeature) {
            gotoAction = String.format(gotoFeature, optimizely.getProjectConfig().getProjectId(), optimizely.getProjectConfig().getFeatureKeyMapping().get(key).getId());
        }
        else {
            gotoAction = String.format(gotoExperiment, optimizely.getProjectConfig().getProjectId(), optimizely.getProjectConfig().getExperimentKeyMapping().get(key).getId());
        }
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(gotoAction));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }
}
