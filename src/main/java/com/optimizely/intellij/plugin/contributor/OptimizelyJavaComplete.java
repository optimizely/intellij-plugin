package com.optimizely.intellij.plugin.contributor;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.ProcessingContext;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import org.jetbrains.annotations.NotNull;
import org.jf.dexlib2.iface.reference.MethodReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class OptimizelyJavaComplete extends CompletionContributor {

    public OptimizelyJavaComplete() {

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(PsiIdentifier.class).withParent(PsiReferenceExpression.class),
                //PlatformPatterns.psiElement(PsiParameter.class).withName("experimentKey").withParent(PsiMethod.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

                        final StringBuilder infoBuilder = new StringBuilder();
                        PsiElement element = parameters.getOriginalPosition();
                        infoBuilder.append("Element at caret: ").append(element).append("\n");
                        if (element == null) return;
                        PsiMethodCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression.class);
                        if (methodCallExpression == null) return;
                        System.out.println("methodexpression " + methodCallExpression.getText());
                        if (
                                !methodCallExpression.getText().endsWith("activate()")
                                && !methodCallExpression.getText().endsWith("isFeatureEnabled()")
                                && !methodCallExpression.getText().endsWith("getFeatureVariableString()")
                                && !methodCallExpression.getText().endsWith("getFeatureVariableDouble()")
                                && !methodCallExpression.getText().endsWith("getFeatureVariableInteger()")
                                && !methodCallExpression.getText().endsWith("getFeatureVariableBoolean()")
                        ) {
                            System.out.println("returning");
                            return;
                        }

                        boolean isExperiment = true;

                        OptimizelyFactoryService factoryService = ServiceManager.getService(OptimizelyFactoryService.class);

                        if (factoryService != null && factoryService.getCurrentOptimizely() != null) {
                            Set<String> keys;
                            if (methodCallExpression.getText().endsWith("activate()")) {
                                keys = factoryService.getCurrentOptimizely().getProjectConfig().getExperimentKeyMapping().keySet();
                            }
                            else {
                                isExperiment = false;
                                keys = factoryService.getCurrentOptimizely().getProjectConfig().getFeatureKeyMapping().keySet();
                            }
                            final boolean setCurrentExperiment = isExperiment;
                            for (String key : keys) {
                                result.addElement(LookupElementBuilder.create(key, "\"" + key + "\"").withInsertHandler(new InsertHandler<LookupElement>() {
                                    @Override
                                    public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
                                        String activeElement = (String)item.getObject();
                                        System.out.println("chose " + activeElement);
                                        if (setCurrentExperiment) {
                                            System.out.println("setting selected experiment key");
                                            factoryService.setSelectedExperimentKey(activeElement);
                                        }
                                        else {
                                            System.out.println("setting selected feature key");
                                            factoryService.setSelectedFeatureKey(activeElement);
                                        }
                                    }
                                }));
                            }
                            result.stopHere();
                        }
                    }
                });
    }
}
