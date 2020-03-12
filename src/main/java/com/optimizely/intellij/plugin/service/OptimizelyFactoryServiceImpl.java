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
import com.optimizely.ab.config.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OptimizelyFactoryServiceImpl implements OptimizelyFactoryService {
    private Map<String, Optimizely> sdkKeyMap = new HashMap<>();
    private Optimizely currentOptimizely;
    private String currentExperimentKey;
    private String currentFeatureFlagKey;
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

            if (optimizely.getOptimizelyConfig() == null) {
                optimizely.close();
                return null;
            }

            sdkKeyMap.put(sdkKey, optimizely);
        }
        currentOptimizely = sdkKeyMap.get(sdkKey);
        currentSDKKey = sdkKey;
        currentExperimentKey = null;
        currentFeatureFlagKey = null;

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
            currentExperimentKey = key;
            currentFeatureFlagKey = null;
        }
    }

    @Override
    public void setSelectedFeatureKey(String key) {
        if (currentOptimizely != null) {
            currentFeatureFlagKey = key;
            currentExperimentKey = null;
        }
    }

    @Override
    public Experiment getSelectedExperiment() { return currentExperimentKey == null? null : Objects.requireNonNull(currentOptimizely.getProjectConfig()).getExperimentForKey(currentExperimentKey, null); }

    @Override
    public FeatureFlag getSelectedFeature() { return currentFeatureFlagKey == null ? null : Objects.requireNonNull(currentOptimizely.getProjectConfig()).getFeatureKeyMapping().get(currentFeatureFlagKey); }
}
