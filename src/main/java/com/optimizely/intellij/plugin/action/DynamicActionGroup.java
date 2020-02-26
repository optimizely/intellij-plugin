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
