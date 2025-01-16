# language: ja
機能: 取引先グループマスタ管理
  管理者として
  取引先グループの管理をしたい
  なぜならマスタデータが必要だから
  背景:
    前提:UC001 ユーザーが登録されている
    前提:UC008 "管理者" である
    前提:UC008 "取引先グループデータ" が登録されている
  シナリオ: 取引先グループ一覧を取得する
    もし:UC008 "取引先グループ一覧" を取得する
    ならば:UC008 "取引先グループ一覧" を取得できる
  シナリオ: 取引先グループを新規登録する
    もし:UC008 取引先グループコード "0010" 取引先グループ名 "食品卸売業" で新規登録する
    ならば:UC008 "取引先グループを登録しました" が表示される
  シナリオ: 取引先グループを取得する
    もし:UC008 取引先グループコード "0010" 取引先グループ名 "食品卸売業" で新規登録する
    もし:UC008 取引先グループコード "0010" で検索する
    ならば:UC008 "食品卸売業" の取引先グループが取得できる
  シナリオ: 登録済み取引先グループを更新登録する
    もし:UC008 取引先グループコード "0010" 取引先グループ名 "食品卸売業" で新規登録する
    かつ:UC008 取引先グループコード "0010" の情報を更新する (取引先グループ名 "飲料卸売業")
    ならば:UC008 "取引先グループを更新しました" が表示される
    もし:UC008 取引先グループコード "0010" で検索する
    ならば:UC008 "飲料卸売業" の取引先グループが取得できる
  シナリオ: 登録済み取引先グループを削除する
    もし:UC008 取引先グループコード "0010" 取引先グループ名 "食品卸売業" で新規登録する
    かつ:UC008 取引先グループコード "0010" を削除する
    ならば:UC008 "取引先グループを削除しました" が表示される
  シナリオ: 取引先グループを検索する
    もし:UC008 取引先グループ名 "取引先グループ" で検索する
    ならば:UC008 取引先グループ検索結果一覧を取得できる