package com.example.sms;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/procurement/purchase/Purchase.feature",
        glue = {"com.example.sms"},
        plugin = {
                "pretty",
                "html:build/reports/tests/purchase-acceptance.html"
                // "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"  // Disabled: Allure plugin disabled
        }
)
public class PurchaseAcceptanceTest {
}
