package com.example.sms;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/",
        glue = {"com.example.sms"},
        plugin = {
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class AcceptanceTests {
}
