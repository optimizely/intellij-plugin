package com.optimizely.intellij.plugin.utils;

import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;

public class OptimizelyUtil {
    public static Boolean isOptimizelyMethod(String text) {
        return text.endsWith("activate()")
                || text.endsWith("getVariation()")
                || text.endsWith("isFeatureEnabled()")
                || text.endsWith("getFeatureVariableString()")
                || text.endsWith("getFeatureVariableDouble()")
                || text.endsWith("getFeatureVariableInteger()")
                || text.endsWith("getFeatureVariableBoolean()");
    }

    public static Boolean isOptimizelyInstanceValid(OptimizelyFactoryService factoryService) {
        return (factoryService != null
                && factoryService.getCurrentOptimizely() != null
                && factoryService.getCurrentOptimizely().isValid());

    }

    public static Boolean isExperimentApi(String text) {
        return text.endsWith("activate()") || text.endsWith("getVariation()");
    }
}
