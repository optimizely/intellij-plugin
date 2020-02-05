package com.optimizely.intellij.plugin.service;

import com.optimizely.ab.Optimizely;

public interface OptimizelyFactoryService {
    Optimizely getBySDKKey(String sdkKey);
    Optimizely getCurrentOptimizely();
}
