// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.optimizely.intellij.plugin.action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dynamic group of Experiments and Features.  Uses JumpToAction to jump to the edit page for Optimizely
 * entity
 *
 * @author Thomas Zurkan
 * @see ActionGroup
 */
public class DynamicActionGroup extends ActionGroup {
  
  /**
   * Returns an array of menu actions for the group.
   *
   * @param  e Event received when the associated group-id menu is chosen.
   * @return AnAction[]  An instance of AnAction, in this case containing a single instance of the
   * ListAction class.
   */
  @NotNull
  @Override
  public AnAction[] getChildren(AnActionEvent e) {
    OptimizelyFactoryService factoryService = ServiceManager.getService(OptimizelyFactoryService.class);
    if (factoryService == null || factoryService.getCurrentOptimizely() == null) {
      return new AnAction[]{};
    }
    String id = ActionManager.getInstance().getId(this);
    if (id.contains("OptimizelyJumptoExperiment")) {
        List<JumpToAction> actions = factoryService.getCurrentOptimizely().getProjectConfig().getExperimentKeyMapping().keySet().stream().map(key -> new JumpToAction(key, key, null, false, key)).collect(Collectors.toList());
        return actions.toArray(new JumpToAction[actions.size()]);
    }
    else {
      List<JumpToAction> actions = factoryService.getCurrentOptimizely().getProjectConfig().getFeatureKeyMapping().keySet().stream().map(key -> new JumpToAction(key, key, null, true, key)).collect(Collectors.toList());
      return actions.toArray(new JumpToAction[actions.size()]);

    }
  }
  
}
