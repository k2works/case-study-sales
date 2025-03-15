package com.example.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleProperties {

    private boolean newFeature;
    private boolean betaFeature;
    private boolean legacyFeature;

    // ゲッターとセッター
    public boolean isNewFeature() {
        return newFeature;
    }

    public void setNewFeature(boolean newFeature) {
        this.newFeature = newFeature;
    }

    public boolean isBetaFeature() {
        return betaFeature;
    }

    public void setBetaFeature(boolean betaFeature) {
        this.betaFeature = betaFeature;
    }

    public boolean isLegacyFeature() {
        return legacyFeature;
    }

    public void setLegacyFeature(boolean legacyFeature) {
        this.legacyFeature = legacyFeature;
    }
}