package com.optimizely.intellij.plugin.contributor;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.ProcessingContext;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.idea.completion.KotlinCompletionCharFilter;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtElement;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import org.jetbrains.kotlin.psi.KtReferenceExpression;
import org.jf.dexlib2.iface.reference.MethodReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class OptimizelyKotlinComplete extends CompletionContributor {

    public OptimizelyKotlinComplete() {

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(LeafPsiElement.class).withParent(KtNameReferenceExpression.class),
                //PlatformPatterns.psiElement(PsiParameter.class).withName("experimentKey").withParent(PsiMethod.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

                        final StringBuilder infoBuilder = new StringBuilder();
                        PsiElement element = parameters.getOriginalPosition();
                        System.out.println("Element at " + element.getText());
                        infoBuilder.append("Element at caret: ").append(element).append("\n");
                        if (element == null) return;
                        KtCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, KtCallExpression.class);
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

                        if (factoryService == null && factoryService.getCurrentOptimizely() == null) return;

                        Set<String> keys;
                        if (methodCallExpression.getText().endsWith("activate()")) {
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
                                    System.out.println("chose " + activeElement);
                                    if (setCurrentExperiment) {
                                        System.out.println("setting selected experiment key");
                                        factoryService.setSelectedExperimentKey(activeElement);
                                    } else {
                                        System.out.println("setting selected feature key");
                                        factoryService.setSelectedFeatureKey(activeElement);
                                    }
                                }
                            }), 1);
                            final boolean[] ranOnce = {false};
                            LookupElementRenderer<LookupElementDecorator<LookupElement>> highlightRenderer =
                                    new LookupElementRenderer<LookupElementDecorator<LookupElement>>() {
                                        @Override
                                        public void renderElement(
                                                LookupElementDecorator<LookupElement> element,
                                                LookupElementPresentation presentation) {
                                            element.getDelegate().renderElement(presentation);
                                            presentation.setTypeText("\n");
                                            final LookupImpl lookup = (LookupImpl)LookupManager.getInstance(parameters.getEditor().getProject()).getActiveLookup();
                                            if (lookup != null) {
                                                //lookup.ensureSelectionVisible(true);
                                                if (!ranOnce[0]) {
//                                                    Runnable after = () -> {lookup.setSelectedIndex(0);};
//                                                    ApplicationManager.getApplication().invokeLater(after);
                                                    Runnable runnable = () -> {lookup.ensureSelectionVisible(true);lookup.setSelectedIndex(0);lookup.setCurrentItem(element);lookup.ensureSelectionVisible(true);};
                                                    ApplicationManager.getApplication().invokeLater(runnable);
                                                    ranOnce[0] = true;
                                                }
                                            }
                                            else {
                                                System.out.println("no lookup impl");
                                            }
                                        }
                                    };
                            item = LookupElementDecorator.withRenderer(item, highlightRenderer);

                            result.addElement(item);
                            //result.runRemainingContributors(parameters, , true);
                            result.stopHere();
                            //AutoPopupController.getInstance(parameters.getEditor().getProject()).scheduleAutoPopup(parameters.getEditor(), CompletionType.BASIC, Condition.TRUE);
                            //AutoPopupController.getInstance(parameters.getEditor().getProject()).scheduleAutoPopup(parameters.getEditor());
                        }
                    }

                });
    }

//    @Override
//    public void fillCompletionVariants(@NotNull CompletionParameters parameters,
//                                       @NotNull CompletionResultSet result) {
//        final CompletionResultSet r = JavaCompletionSorting.addJavaSorting(parameters, result.withPrefixMatcher("\""));
//        super.fillCompletionVariants(parameters, r);
//    }
}
