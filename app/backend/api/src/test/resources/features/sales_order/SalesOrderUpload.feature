# language: ja
機能: 受注管理
  管理者として
  受注業務を管理したい
  なぜなら受注データの一元管理が必要だから
  背景:
    前提:UC001 ユーザーが登録されている
    前提:UC015 "管理者" である
    前提:UC015 "部門データ" が登録されている
    前提:UC015 "社員データ" が登録されている
    前提:UC015 "取引先データ" が登録されている
    前提:UC015 "商品データ" が登録されている
    前提:UC015 "受注データ" が登録されている

  シナリオ: 受注を新規登録する
    もし:UC015 "受注データ" をアップロードする
    ならば:UC015 "受注をアップロードしました" が表示される

  シナリオ: 受注を新規登録する
    もし:UC015 エラーのある "受注データ" をアップロードする
    ならば:UC015 "受注アップロード内容にエラーがあります" が表示される
