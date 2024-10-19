# language: ja

機能: ユーザー認証

  管理者として
  アプリケーションユーザーの認証をしたい
  なぜなら認証されたユーザーしか利用できないから

  背景:
    前提:UC001 ユーザーが登録されている

  シナリオ: 管理者を認証する
    もし:UC001 "管理者権限" でログインする
    ならば:UC001 "管理者" として認証される

  シナリオ: ユーザーを認証する
    もし:UC001 "利用者権限" でログインする
    ならば:UC001 "利用者" として認証される
