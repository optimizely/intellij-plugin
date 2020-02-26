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
import com.optimizely.ab.config.HttpProjectConfigManager;
import com.optimizely.ab.config.ProjectConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OptimizelyFactoryServiceImpl implements OptimizelyFactoryService {
    private Map<String, Optimizely> sdkKeyMap = new HashMap<>();
    private Optimizely currentOptimizely;
    private Experiment currentExperiment;
    private FeatureFlag currentFeatureFlag;
    private String currentSDKKey;

    @Override
    public Optimizely getBySDKKey(String sdkKey) {
        if (!sdkKeyMap.containsKey(sdkKey)) {
            ProjectConfigManager projectConfigManager = HttpProjectConfigManager.builder()
                    .withSdkKey(sdkKey)
                    .withPollingInterval(1L, TimeUnit.MINUTES)
                    .build();

            Optimizely optimizely = Optimizely.builder()
                    .withConfigManager(projectConfigManager)
                    .build();

            sdkKeyMap.put(sdkKey, optimizely);

        }
        currentOptimizely = sdkKeyMap.get(sdkKey);
        currentSDKKey = sdkKey;

        return currentOptimizely;
    }

    @Override
    public String getCurrentSDKKey() {
        return currentSDKKey;
    }

    @Override
    public Optimizely getCurrentOptimizely() {
        return currentOptimizely;
    }

    @Override
    public void setSelectedExperimentKey(String key) {
        if (currentOptimizely != null) {
            currentExperiment = currentOptimizely.getProjectConfig().getExperimentForKey(key, null);
            currentFeatureFlag = null;
        }
    }

    @Override
    public void setSelectedFeatureKey(String key) {
        if (currentOptimizely != null) {
            currentFeatureFlag = currentOptimizely.getProjectConfig().getFeatureKeyMapping().get(key);
            currentExperiment = null;
        }
    }

    @Override
    public Experiment getSelectedExperiment() {
        return currentExperiment;
    }

    @Override
    public FeatureFlag getSelectedFeature() {
        return currentFeatureFlag;
    }
}
