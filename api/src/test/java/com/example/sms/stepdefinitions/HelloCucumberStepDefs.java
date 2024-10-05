package com.example.sms.stepdefinitions;

import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloCucumberStepDefs {
    private String today;
    private String actualAnswer;

    static class IsItFriday {
        static String isItFriday(String today) {
            return "金曜日".equals(today) ? "花金" : "否";
        }
    }

    @前提("今日が {string} である")
    public void 今日がである(String today) {
        this.today = today;
    }

    @もし("私が今が金曜日かどうか聞く")
    public void 私が今が金曜日かどうか聞く() {
        actualAnswer = IsItFriday.isItFriday(today);
    }

    @ならば("{string} と教えてくれるべき")
    public void と教えてくれるべき(String expectedAnswer) {
        assertEquals(expectedAnswer, actualAnswer);
    }
}
