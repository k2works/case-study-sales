# language: ja
機能: 取引先マスタ管理
  管理者として
  取引先マスタの管理をしたい
  なぜなら業務運営のために取引先データが必要だから
  背景:
    前提:UC001 ユーザーが登録されている
    前提:UC011 "管理者" である
    前提:UC011 "取引先データ" が登録されている

  シナリオ: 取引先一覧を取得する
    もし:UC011 "取引先一覧" を取得する
    ならば:UC011 "取引先一覧" を取得できる

  シナリオ: 新規取引先を登録する
    もし:UC011 取引先コード "009" 名前 "株式会社サンプル" で新規登録する
    ならば:UC011 "取引先を登録しました" が表示される

  シナリオ: 取引先情報を取得する
    もし:UC011 取引先コード "009" 名前 "株式会社サンプル" で新規登録する
    もし:UC011 取引先コード "009" で検索する
    ならば:UC011 "株式会社サンプル" の取引先が取得できる

  シナリオ: 登録済み取引先を更新登録する
    もし:UC011 取引先コード "009" 名前 "株式会社サンプル" で新規登録する
    かつ:UC011 取引先コード "009" の情報を更新する (名前 "株式会社アップデート")
    ならば:UC011 "取引先を更新しました" が表示される
    もし:UC011 取引先コード "009" で検索する
    ならば:UC011 "株式会社アップデート" を取得できる

  シナリオ: 登録済み取引先を削除する
    もし:UC011 取引先コード "009" 名前 "株式会社サンプル" で新規登録する
    かつ:UC011 取引先コード "009" を削除する
    ならば:UC011 "取引先を削除しました" が表示される
    もし:UC011 取引先コード "009" で検索する
    ならば:UC011 "該当する取引先が存在しません" が表示される

  シナリオ: 取引先を検索する
    もし:UC011 名前 "取引先" で検索する
    ならば:UC011 取引先検索結果一覧を取得できる