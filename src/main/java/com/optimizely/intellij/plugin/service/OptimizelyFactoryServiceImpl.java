package com.optimizely.intellij.plugin.service;

import com.optimizely.ab.Optimizely;
import com.optimizely.ab.config.HttpProjectConfigManager;
import com.optimizely.ab.config.ProjectConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OptimizelyFactoryServiceImpl implements OptimizelyFactoryService {
    private Map<String, Optimizely> sdkKeyMap = new HashMap<>();
    private Optimizely currentOptimizely;

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
        return currentOptimizely;
    }

    @Override
    public Optimizely getCurrentOptimizely() {
        return currentOptimizely;
    }
}
