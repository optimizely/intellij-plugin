package com.optimizely.intellij.plugin.contributor.python;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementDecorator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.python.psi.PyCallExpression;
import com.jetbrains.python.psi.PyElement;
import com.jetbrains.python.psi.PyExpressionStatement;
import com.jetbrains.python.psi.PyReferenceExpression;
import com.jetbrains.python.psi.impl.PyExpressionStatementImpl;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import com.optimizely.intellij.plugin.utils.OptimizelyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class OptimizelyPythonComplete extends CompletionContributor {

    public OptimizelyPythonComplete() {

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(PsiElement.class),
                //PlatformPatterns.psiElement(PsiParameter.class).withName("experimentKey").withParent(PsiMethod.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

                        System.out.println("got here");
                        PsiElement element = parameters.getOriginalPosition();
                        if (element == null) return;
                        System.out.println(element.getText());
                        System.out.println (element.toString());
                        PsiElement parent = element.getParent();
//                        while (parent != null) {
//                            System.out.println("Parent");
//                            System.out.println(parent.getText());
//                            System.out.println(parent.toString());
//                            System.out.println(parent.getClass().getName());
//                            parent = parent.getParent();
//                        }
                        PyCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, PyCallExpression.class);
                        if (methodCallExpression == null) return;
                        System.out.println(methodCallExpression.getText());
                        if (!OptimizelyUtil.isOptimizelyMethodCamelCase(methodCallExpression.getText())) {
                            return;
                        }

                        boolean isExperiment = true;

                        OptimizelyFactoryService factoryService = ServiceManager.getService(OptimizelyFactoryService.class);

                        if (!OptimizelyUtil.isOptimizelyInstanceValid(factoryService)) return;

                        Set<String> keys;
                        if (OptimizelyUtil.isExperimentApiCamelCase(methodCallExpression.getText())) {
                            keys = factoryService.getCurrentOptimizely().getProjectConfig().getExperimentKeyMapping().keySet();
                        }
                        else {
                            isExperiment = false;
                            keys = factoryService.getCurrentOptimizely().getProjectConfig().getFeatureKeyMapping().keySet();
                        }
                        final boolean setCurrentExperiment = isExperiment;
                        for (String key : keys) {
                            LookupElement lookupElement = LookupElementBuilder.create(key, "\"" + key + "\"").withInsertHandler(new InsertHandler<LookupElement>() {
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

                            lookupElement = PrioritizedLookupElement.withGrouping(lookupElement, 79);
                            lookupElement = PrioritizedLookupElement.withPriority(lookupElement, 1);
                            lookupElement = PrioritizedLookupElement.withExplicitProximity(lookupElement, 0);

                            result.addElement(lookupElement);
                        }
                        //result.stopHere();
                    }
                });
    }
}
