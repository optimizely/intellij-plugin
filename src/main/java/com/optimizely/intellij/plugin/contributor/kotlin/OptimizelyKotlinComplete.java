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
package com.optimizely.intellij.plugin.contributor.kotlin;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.IconManager;
import com.intellij.util.IconUtil;
import com.intellij.util.ProcessingContext;
import com.optimizely.intellij.plugin.contributor.java.OptimizelyJavaComplete;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import com.optimizely.intellij.plugin.utils.OptimizelyUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import org.jetbrains.kotlin.psi.KtStringTemplateExpression;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class OptimizelyKotlinComplete extends CompletionContributor {
    static int priority = 10000;

    public OptimizelyKotlinComplete() {

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(LeafPsiElement.class),
                //PlatformPatterns.psiElement(PsiParameter.class).withName("experimentKey").withParent(PsiMethod.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

                        PsiElement element = parameters.getOriginalPosition();
                        if (element == null) return;
                        KtCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, KtCallExpression.class);
                        if (methodCallExpression == null) return;
                        if (!OptimizelyUtil.isOptimizelyMethod(methodCallExpression.getText())) {
                            if (OptimizelyUtil.isGetFeatureSecondParameter(methodCallExpression.getText())) {
                                OptimizelyJavaComplete.fillVariation(methodCallExpression.getText(), result);
                            }
                            return;
                        }

                        boolean isExperiment = true;

                        OptimizelyFactoryService factoryService = ServiceManager.getService(OptimizelyFactoryService.class);

                        if (!OptimizelyUtil.isOptimizelyInstanceValid(factoryService)) return;

                        Set<String> keys;
                        if (OptimizelyUtil.isExperimentApi(methodCallExpression.getText())) {
                            keys = factoryService.getCurrentOptimizely().getProjectConfig().getExperimentKeyMapping().keySet();
                        } else {
                            isExperiment = false;
                            keys = factoryService.getCurrentOptimizely().getProjectConfig().getFeatureKeyMapping().keySet();
                        }
                        final boolean setCurrentExperiment = isExperiment;

                        for (String key : keys) {

                            LookupElement lookupElement = LookupElementBuilder.create("\"" + key + "\"").withTypeText("String").withInsertHandler(new InsertHandler<LookupElement>() {
                                @Override
                                public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
                                    String activeElement = (String)item.getObject();
                                    if (setCurrentExperiment) {
                                        factoryService.setSelectedExperimentKey(activeElement);
                                    }
                                    else {
                                        factoryService.setSelectedFeatureKey(activeElement);
                                    }
                                }
                            });

                            //lookupElement = PrioritizedLookupElement.withGrouping(lookupElement, 79);
                            lookupElement = PrioritizedLookupElement.withPriority(lookupElement, priority++);
                            lookupElement = PrioritizedLookupElement.withExplicitProximity(lookupElement, 0);

                            result.addElement(lookupElement);

                            //result.stopHere();
                            //AutoPopupController.getInstance(parameters.getEditor().getProject()).scheduleAutoPopup(parameters.getEditor(), CompletionType.BASIC, Condition.TRUE);
                            //AutoPopupController.getInstance(parameters.getEditor().getProject()).scheduleAutoPopup(parameters.getEditor());
                        }
                    }

                });
    }

}
