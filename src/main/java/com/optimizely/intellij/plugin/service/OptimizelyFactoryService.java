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
package com.optimizely.intellij.plugin.service;

import com.optimizely.ab.Optimizely;
import com.optimizely.ab.config.Experiment;
import com.optimizely.ab.config.FeatureFlag;

public interface OptimizelyFactoryService {
    Optimizely getBySDKKey(String sdkKey);
    Optimizely getCurrentOptimizely();
    String getCurrentSDKKey();
    void setSelectedExperimentKey(String key);
    void setSelectedFeatureKey(String key);
    Experiment getSelectedExperiment();
    FeatureFlag getSelectedFeature();
}
