// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

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
 * @author Anna Bulenkova
 * @author jhake
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
