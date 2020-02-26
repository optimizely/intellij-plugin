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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import icons.ActionBasicsIcons;
import org.jetbrains.annotations.Nullable;

/**
 * Creates an action group to contain menu actions. See plugin.xml declarations.
 * @author Thomas Zurkan
 */
public class OptimizelyActionGroup extends DefaultActionGroup {

  /**
   * Given OptimizelyActionGroup is derived from ActionGroup, in this context
   * update() determines whether the action group itself should be enabled or disabled.
   * Requires an editor to be active in order to enable the group functionality.
   * @see com.intellij.openapi.actionSystem.AnAction#update(AnActionEvent)
   * @param event  Event received when the associated group-id menu is chosen.
   */
  @Override
  public void update(AnActionEvent event) {
    // Enable/disable depending on whether user is editing
    Editor editor = event.getData(CommonDataKeys.EDITOR);
    event.getPresentation().setEnabled(editor != null);
    String sdkKey = ServiceManager.getService(OptimizelyFactoryService.class).getCurrentSDKKey();
    if (sdkKey != null) {
      event.getPresentation().setText("Optimizely " + sdkKey);
    }
    else {
      event.getPresentation().setText("Optimizely");
    }
    // Take this opportunity to set an icon for the menu entry.
    event.getPresentation().setIcon(ActionBasicsIcons.Sdk_default_icon);
  }
}
