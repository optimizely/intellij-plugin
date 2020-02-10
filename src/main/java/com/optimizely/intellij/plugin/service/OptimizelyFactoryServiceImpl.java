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
