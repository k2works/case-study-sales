# language: ja
機能: 地域コードマスタ管理
  管理者として
  地域コードの管理をしたい
  なぜならマスタデータが必要だから
  背景:
    前提:UC001 ユーザーが登録されている
    前提:UC010 "管理者" である
    前提:UC010 "地域コードデータ" が登録されている
  シナリオ: 地域コード一覧を取得する
    もし:UC010 "地域コード一覧" を取得する
    ならば:UC010 "地域コード一覧" を取得できる
  シナリオ: 地域コードを新規登録する
    もし:UC010 地域コード "R009" 地域名 "北海道" で新規登録する
    ならば:UC010 "地域コードを登録しました" が表示される
  シナリオ: 地域コードを取得する
    もし:UC010 地域コード "R009" 地域名 "北海道" で新規登録する
    もし:UC010 地域コード "R009" で検索する
    ならば:UC010 "北海道" の地域コードが取得できる
  シナリオ: 登録済み地域コードを更新登録する
    もし:UC010 地域コード "R009" 地域名 "北海道" で新規登録する
    かつ:UC010 地域コード "R009" の情報を更新する (地域名 "東北")
    ならば:UC010 "地域コードを更新しました" が表示される
    もし:UC010 地域コード "R009" で検索する
    ならば:UC010 "東北" の地域コードが取得できる
  シナリオ: 登録済み地域コードを削除する
    もし:UC010 地域コード "R009" 地域名 "北海道" で新規登録する
    かつ:UC010 地域コード "R009" を削除する
    ならば:UC010 "地域コードを削除しました" が表示される
  シナリオ: 地域コードを検索する
    もし:UC010 地域名 "地域名" で検索する
    ならば:UC010 地域コード検索結果一覧を取得できる