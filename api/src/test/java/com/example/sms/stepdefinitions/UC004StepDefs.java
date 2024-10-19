package com.example.sms.stepdefinitions;

import io.cucumber.java.ja.かつ;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;

public class UC004StepDefs {
    @前提(":UC004 {string} である")
    public void login(String user) {
    }

    @前提(":UC004 {string} が登録されている")
    public void init(String data) {
    }

    @もし(":UC004 {string} を取得する")
    public void list(String employees) {
    }

    @ならば(":UC004 {string} を取得できる")
    public void find(String employee) {
    }

    @もし(":UC004 社員コード {string} 社員名 {string} で新規登録する")
    public void regist(String code, String name) {
    }

    @ならば(":UC004 {string} が表示される")
    public void show(String employee) {
    }

    @もし(":UC004 社員コード {string} で検索する")
    public void search(String code) {
    }

    @かつ(":UC004 社員コード {string} の情報を更新する \\(社員名 {string})")
    public void update(String code, String name) {
    }

    @かつ(":UC004 社員コード {string} を削除する")
    public void delete(String code) {
    }
}
