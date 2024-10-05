package com.example.sms;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "classpath:features/Auth.feature",
        },
        glue = {"com.example.sms"}
)
public class AcceptanceTest {
}
