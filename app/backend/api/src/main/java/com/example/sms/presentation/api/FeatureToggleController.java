package com.example.sms.presentation.api;

import com.example.sms.FeatureToggleProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/features")
@Tag(name = "FeatureToggle", description = "フィーチャートグル")
@PreAuthorize("hasRole('ADMIN')")
public class FeatureToggleController {

    private final FeatureToggleProperties featureToggleProperties;
    private final Set<String> allowedFeatures = Set.of("newFeature", "betaFeature", "legacyFeature");

    // コンストラクタインジェクション
    public FeatureToggleController(FeatureToggleProperties featureToggleProperties) {
        this.featureToggleProperties = featureToggleProperties;
    }

    // 現在のフィーチャートグルの状態を取得
    @GetMapping
    public FeatureToggleProperties getFeatureToggles() {
        return featureToggleProperties; // 現在のプロパティすべてを返す
    }

    // フィーチャートグルの特定の値を動的に変更する例（開発・テスト向け）
    @PostMapping("/{featureName}/toggle")
    public String toggleFeature(@PathVariable String featureName, @RequestParam boolean enabled) {
        if (!allowedFeatures.contains(featureName)) {
            return "Feature not found or not allowed: " + featureName;
        }

        switch (featureName) {
            case "newFeature":
                featureToggleProperties.setNewFeature(enabled);
                break;
            case "betaFeature":
                featureToggleProperties.setBetaFeature(enabled);
                break;
            case "legacyFeature":
                featureToggleProperties.setLegacyFeature(enabled);
                break;
            default:
                // ここには到達しないはずですが、安全のため例外をスロー
                throw new IllegalArgumentException("Unexpected feature: " + featureName);
        }
        return "Feature " + featureName + " set to " + enabled;
    }
}