package com.example.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleProperties {

    // ゲッターとセッター
    private boolean newFeature;
    private boolean betaFeature;
    private boolean legacyFeature;

}