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
