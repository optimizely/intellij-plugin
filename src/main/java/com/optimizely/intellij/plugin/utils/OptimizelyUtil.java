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
package com.optimizely.intellij.plugin.utils;

import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;

public class OptimizelyUtil {
    public static Boolean isOptimizelyMethod(String text) {
        return isExperimentApi(text)
                || text.endsWith("isFeatureEnabled()")
                || text.endsWith("getFeatureVariableString()")
                || text.endsWith("getFeatureVariableDouble()")
                || text.endsWith("getFeatureVariableInteger()")
                || text.endsWith("getFeatureVariableBoolean()");
    }

    public static Boolean isOptimizelyMethodCamelCase(String text) {
        return isExperimentApiCamelCase(text)
                || text.endsWith("is_feature_enabled()")
                || text.endsWith("get_feature_variable()")
                || text.endsWith("get_feature_variable_string()")
                || text.endsWith("get_feature_variable_double()")
                || text.endsWith("get_feature_variable_integer()")
                || text.endsWith("get_feature_variable_boolean()");
    }

    public static Boolean isOptimizelyMethodGo(String text) {
        return isExperimentApiGo(text)
                || text.endsWith("IsFeatureEnabled()")
                || text.endsWith("GetFeatureVariable()")
                || text.endsWith("GetFeatureVariableString()")
                || text.endsWith("GetFeatureVariableDouble()")
                || text.endsWith("GetFeatureVariableInteger()")
                || text.endsWith("GetFeatureVariableBoolean()");
    }

    public static Boolean isOptimizelyInstanceValid(OptimizelyFactoryService factoryService) {
        return (factoryService != null
                && factoryService.getCurrentOptimizely() != null
                && factoryService.getCurrentOptimizely().isValid());

    }

    public static Boolean isExperimentApiGo(String text) {
        return text.endsWith("Activate()") || text.endsWith("GetVariation()");
    }

    public static Boolean isExperimentApi(String text) {
        return text.endsWith("activate()") || text.endsWith("getVariation()");
    }

    public static Boolean isExperimentApiCamelCase(String text) {
        return text.endsWith("activate()") || text.endsWith("get_variation()");
    }
}
