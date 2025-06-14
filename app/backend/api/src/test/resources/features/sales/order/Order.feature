# language: ja
機能: 受注管理
  管理者として
  受注業務を管理したい
  なぜなら受注データの一元管理が必要だから
  背景:
    前提:UC001 ユーザーが登録されている
    前提:UC014 "管理者" である
    前提:UC014 "顧客データ" が登録されている
    前提:UC014 "商品データ" が登録されている
    前提:UC014 "受注データ" が登録されている

  シナリオ: 受注一覧を取得する
    もし:UC014 "受注一覧" を取得する
    ならば:UC014 "受注一覧" を取得できる

  シナリオ: 受注を新規登録する
    もし:UC014 受注番号 "OD00000009" 受注日 "2024-11-01T00:00:00+09:00" 部門コード "10000" 顧客コード "009" 社員コード "EMP001" 希望納期 "2024-11-10T00:00:00+09:00" で新規登録する
    ならば:UC014 "受注を登録しました" が表示される

  シナリオ: 受注情報を取得する
    もし:UC014 受注番号 "OD00000009" 受注日 "2024-11-01T00:00:00+09:00" 部門コード "10000" 顧客コード "009" 社員コード "EMP001" 希望納期 "2024-11-10T00:00:00+09:00" で新規登録する
    もし:UC014 受注番号 "OD00000009" で検索する
    ならば:UC014 受注番号 "OD00000009" の受注情報を取得できる

  シナリオ: 登録済み受注を更新する
    もし:UC014 受注番号 "OD00000009" 受注日 "2024-11-01T00:00:00+09:00" 部門コード "10000" 顧客コード "009" 社員コード "EMP001" 希望納期 "2024-11-10T00:00:00+09:00" で新規登録する
    かつ:UC014 受注番号 "OD00000009" の情報を更新する (希望納期 "2024-11-15T00:00:00+09:00")
    ならば:UC014 "受注を更新しました" が表示される
    もし:UC014 受注番号 "OD00000009" で検索する
    ならば:UC014 希望納期 "2024-11-15T00:00" を含む受注情報が取得できる

  シナリオ: 登録済み受注を削除する
    もし:UC014 受注番号 "OD00000009" 受注日 "2024-11-01T00:00:00+09:00" 部門コード "10000" 顧客コード "009" 社員コード "EMP001" 希望納期 "2024-11-10T00:00:00+09:00" で新規登録する
    かつ:UC014 受注番号 "OD00000009" を削除する
    ならば:UC014 "受注を削除しました" が表示される

  シナリオ: 受注明細を追加登録する
    もし:UC014 受注番号 "OD00000009" をもとに以下の受注明細を登録する
      | 受注番号    | 枝番 | 商品コード | 商品名   | 数量 | 単価 |
      | OD00000009 | 1   | 10101001   | 鶏ささみ | 10   | 500  |
    ならば:UC014 "受注を登録しました" が表示される
    もし:UC014 受注番号 "OD00000009" で検索する
    ならば:UC014 明細データに商品コード "10101001" が含まれる

  シナリオ: 登録済み受注明細を更新する
    もし:UC014 受注番号 "OD00000009" をもとに以下の受注明細を登録する
      | 受注番号    | 枝番 | 商品コード | 商品名   | 数量 | 単価 |
      | OD00000009 | 1   | 10101001   | 鶏ささみ | 10   | 500  |
    かつ:UC014 受注番号 "OD00000009" の受注明細を更新する (数量 "15")
    ならば:UC014 "受注を更新しました" が表示される
    もし:UC014 受注番号 "OD00000009" で検索する
    ならば:UC014 明細データに数量 "15" の商品コード "10101001" が含まれる

  シナリオ: 登録済み受注明細を削除する
    もし:UC014 受注番号 "OD00000009" をもとに以下の受注明細を登録する
      | 受注番号    | 枝番 | 商品コード | 商品名   | 数量 | 単価 |
      | OD00000009 | 1   | 10101001   | 鶏ささみ | 10   | 500  |
    かつ:UC014 受注番号 "OD00000009" 商品コード "10101001" の受注明細を削除する
    ならば:UC014 "受注を更新しました" が表示される

  シナリオ: 顧客ごとの受注を検索する
    もし:UC014 顧客コード "001" で受注を検索する
    ならば:UC014 検索結果として受注一覧を取得できる