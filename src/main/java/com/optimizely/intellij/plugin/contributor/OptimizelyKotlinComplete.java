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
package com.optimizely.intellij.plugin.contributor;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import com.optimizely.intellij.plugin.utils.OptimizelyUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;

import java.util.Set;

public class OptimizelyKotlinComplete extends CompletionContributor {

    public OptimizelyKotlinComplete() {

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(LeafPsiElement.class).withParent(KtNameReferenceExpression.class),
                //PlatformPatterns.psiElement(PsiParameter.class).withName("experimentKey").withParent(PsiMethod.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

                        PsiElement element = parameters.getOriginalPosition();
                        if (element == null) return;
                        KtCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, KtCallExpression.class);
                        if (methodCallExpression == null) return;
                        if (!OptimizelyUtil.isOptimizelyMethod(methodCallExpression.getText())) {
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

                            LookupElement item = PrioritizedLookupElement.withPriority(LookupElementBuilder.create(key, "\"" + key + "\"").withInsertHandler(new InsertHandler<LookupElement>() {
                                @Override
                                public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
                                    String activeElement = (String) item.getObject();
                                    if (setCurrentExperiment) {
                                        factoryService.setSelectedExperimentKey(activeElement);
                                    } else {
                                        factoryService.setSelectedFeatureKey(activeElement);
                                    }
                                }
                            }), 1);

                            LookupElementRenderer<LookupElementDecorator<LookupElement>> highlightRenderer =
                                    new LookupElementRenderer<LookupElementDecorator<LookupElement>>() {
                                        @Override
                                        public void renderElement(
                                                LookupElementDecorator<LookupElement> element,
                                                LookupElementPresentation presentation) {
                                            element.getDelegate().renderElement(presentation);
                                            presentation.setTypeText("\n");
//                                            final LookupImpl lookup = (LookupImpl)LookupManager.getInstance(parameters.getEditor().getProject()).getActiveLookup();
//                                            if (lookup != null) {
//                                                if (!ranOnce[0]) {
//                                                    Runnable runnable = () -> {lookup.setSelectedIndex(0);lookup.setCurrentItem(element);lookup.ensureSelectionVisible(true);};
//                                                    Runnable after = () -> {lookup.setLastVisibleIndex(0);};
//                                                    ApplicationManager.getApplication().invokeLater(runnable);
//                                                    ApplicationManager.getApplication().invokeLater(after);
//                                                    ranOnce[0] = true;
//                                                }
//                                            }
//                                            else {
//                                                System.out.println("no lookup impl");
//                                            }
                                        }
                                    };
                            item = LookupElementDecorator.withRenderer(item, highlightRenderer);

                            result.addElement(item);
                            //result.stopHere();
                            //AutoPopupController.getInstance(parameters.getEditor().getProject()).scheduleAutoPopup(parameters.getEditor(), CompletionType.BASIC, Condition.TRUE);
                            //AutoPopupController.getInstance(parameters.getEditor().getProject()).scheduleAutoPopup(parameters.getEditor());
                        }
                    }

                });
    }

}
