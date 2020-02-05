package com.optimizely.intellij.plugin.service;

import com.optimizely.ab.Optimizely;
import com.optimizely.ab.config.Experiment;
import com.optimizely.ab.config.FeatureFlag;

public interface OptimizelyFactoryService {
    Optimizely getBySDKKey(String sdkKey);
    Optimizely getCurrentOptimizely();
    void setSelectedExperimentKey(String key);
    void setSelectedFeatureKey(String key);
    Experiment getSelectedExperiment();
    FeatureFlag getSelectedFeature();
}
