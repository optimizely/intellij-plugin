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
package com.optimizely.intellij.plugin.contributor.python;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.python.psi.PyCallExpression;
import com.optimizely.ab.optimizelyconfig.OptimizelyFeature;
import com.optimizely.ab.optimizelyconfig.OptimizelyVariable;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import com.optimizely.intellij.plugin.utils.OptimizelyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OptimizelyPythonComplete extends CompletionContributor {

    public OptimizelyPythonComplete() {

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(PsiElement.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

                        PsiElement element = parameters.getOriginalPosition();
                        if (element == null) return;
                        PsiElement parent = element.getParent();
                        PyCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, PyCallExpression.class);
                        if (methodCallExpression == null) return;
                        if (!OptimizelyUtil.isOptimizelyMethodCamelCase(methodCallExpression.getText())) {
                            if (OptimizelyUtil.isGetFeatureSecondParameterCamelCase(methodCallExpression.getText())) {
                                fillVariation(methodCallExpression.getText(), result);
                            }
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

                            //lookupElement = PrioritizedLookupElement.withGrouping(lookupElement, 79);
                            lookupElement = PrioritizedLookupElement.withPriority(lookupElement, 1000);
                            lookupElement = PrioritizedLookupElement.withExplicitProximity(lookupElement, 1);

                            result.addElement(lookupElement);
                        }
                        //result.stopHere();
                    }
                });
    }

    public void fillVariation(String text, CompletionResultSet result) {
        OptimizelyFactoryService factoryService = ServiceManager.getService(OptimizelyFactoryService.class);

        if (!OptimizelyUtil.isOptimizelyInstanceValid(factoryService)) return;

        int start = text.indexOf("\"") + 1;
        int end = text.indexOf("\"", start);

        String featureKey = text.substring(start, end);

        OptimizelyFeature feature = factoryService.getCurrentOptimizely().getOptimizelyConfig().getFeaturesMap().get(featureKey);

        if (feature == null) return;

        String filter = "";
        if (text.matches(OptimizelyUtil.regexPrefix + "get_feature_variable_string" + OptimizelyUtil.regex)) {
            filter = "string";
        }
        else if (text.matches(OptimizelyUtil.regexPrefix + "get_feature_variable_integer" + OptimizelyUtil.regex)) {
            filter = "integer";
        }
        else if (text.matches(OptimizelyUtil.regexPrefix + "get_feature_variable_double" + OptimizelyUtil.regex)) {
            filter = "double";
        }
        else if (text.matches(OptimizelyUtil.regexPrefix + "get_feature_variable_boolean" + OptimizelyUtil.regex)) {
            filter = "boolean";
        }

        String finalFilter = filter;
        List<String> vKeys = feature.getVariablesMap().keySet().stream().filter((String k) -> {
            OptimizelyVariable v = feature.getVariablesMap().get(k);
            if (!finalFilter.isEmpty()) {
                if (finalFilter == v.getType()) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }).collect(Collectors.toList());

        for (String key : vKeys) {
            LookupElement lookupElement = LookupElementBuilder.create(key, "\"" + key + "\"");
            lookupElement = PrioritizedLookupElement.withGrouping(lookupElement, 79);
            lookupElement = PrioritizedLookupElement.withPriority(lookupElement, 1);
            lookupElement = PrioritizedLookupElement.withExplicitProximity(lookupElement, 0);

            result.addElement(lookupElement);
        }
    }

}
