import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* 取引先管理
-- ユースケース
--- 受注一覧
--- 受注詳細
--- 受注検索
--- 受注登録
--- 受注編集
--- 受注削除
--- 受注ダウンロード
--- 受注アップロード
--- 受注ルール
-- ドメインモデル
--- 受注一覧
--- 受注
--- 受注明細
--- 受注ルール
-- データモデル
--- 受注
--- 受注明細
** ユーザーインターフェース
*** ビュー
**** ナビゲーション
**** 受注一覧
**** 受注
**** 受注一括登録
**** アップロード
**** 受注ルール一覧
*** モデル
**** ナビゲーション
**** 受注一覧
**** 受注
**** 受注一括登録
**** アップロード
**** 受注ルール一覧
*** インタラクション
**** ナビゲーション
**** 受注一覧
**** 受注
**** 受注一括登録
**** アップロード
**** 受注ルール一覧

@endmindmap
`;

const contents = `
## 受注管理

- 管理者と利用者は受注を管理できる
- 管理者と利用者は受注データをアップロード登録できる
- 管理者と利用者は受注ルールを確認できる

## TODOリスト

- [x] 受注APIを作成する
  - [x] 受注一覧
  - [x] 受注詳細
  - [x] 受注検索
  - [x] 受注登録
    - [x] 注文合計金額計算
    - [x] 注文消費税合計計算
  - [x] 受注編集
  - [x] 受注削除
  - [x] 受注ダウンロード
  - [x] 受注アップロード
  - [x] 受注ルールチェック
- [x] 受注UIを作成する
  - [x] 受注一覧
  - [x] 受注詳細
  - [x] 受注検索
  - [x] 受注一括登録
  - [x] 受注ルール一覧
- [x] 利用者のテストケース追加
  - [x] マスタデータの一覧権限変更
  - [x] ダウンロードの権限変更

`;

const usecase = `
@startuml
left to right direction
actor "管理者" as admin
actor "利用者" as user

rectangle 受注管理 {
    (受注一覧)
    (受注詳細)
    (受注検索)
    (受注登録)
    (受注編集)
    (受注削除)
    (受注ダウンロード)
    (受注アップロード)
    (受注ルール確認)
}

admin --> 受注一覧
admin --> 受注詳細
admin --> 受注検索
admin --> 受注登録
admin --> 受注編集
admin --> 受注削除
admin --> 受注ダウンロード
admin --> 受注アップロード
admin --> 受注ルール確認
user --> 受注一覧
user --> 受注詳細
user --> 受注検索
user --> 受注登録
user --> 受注編集
user --> 受注削除
user --> 受注ダウンロード
user --> 受注アップロード
user --> 受注ルール確認

@enduml
`;

const uml = `
@startuml
class 受注一覧
class 受注
class 受注明細

受注一覧 *-- 受注
受注 *- 受注明細
受注ルール -> 受注

@enduml
`;

const erd = `
@startuml

entity 受注 {
    + 受注ID [PK]
    --
    + 受注日
    + 受注金額
    + 受注明細
}

entity 受注明細 {
    + 受注明細ID [PK]
    --
    + 受注ID [FK]
    + 商品ID [FK]
    + 数量
    + 単価
}

entity 受注ルール履歴 {
    + ID [PK]
    --
    + ルール名
    + ルール内容
    + 受注ID [FK]
}


受注 ||--o{ 受注明細
受注 -o{ 受注ルール履歴
@enduml
`;

const ui = `
@startsalt
{+
  受注一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      受注
      ログアウト
      } |
      {
        {
          {/ <b>一覧 | 一括登録 | ルール}
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }
      {+
        ---------------------
        {
          1xxxxxx | 商品1    | [  編集  ] | [  削除  ]
          1xxxxxx | 商品2    | [  編集  ] | [  削除  ]
          1xxxxxx | 商品3    | [  編集  ] | [  削除  ]
        }
      }
     }
  }
  ----------------
  受注画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        受注番号    | "1xxxxxx"   "
        商品名        | "商品1    "
      }
      <b>明細</b>
      [ 追加 ]
      ---------------------
      {
        1xxxxxx | 枝番1    | [  削除  ]
        1xxxxxx | 枝番2    | [  削除  ]
        1xxxxxx | 枝番3    | [  削除  ]
      }
  }
  ----------------
  受注一括登録画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      受注
      ログアウト
      } |
      {
        {
          {/ 一覧 | <b>一括登録 | ルール}
          [ アップロード ] 
        }
      {+
        ---------------------
        {
          エラーメッセージ | [削除]
          エラーメッセージ | [削除]
          エラーメッセージ | [削除]
        }
      }
     }
  }
  ----------------
  アップロード画面（シングル）
  {+
      {
        ファイル名:  | { "sales_order.csv" | [選択] }
        [  アップロード  ] | [  キャンセル  ]
      }
  }
  ----------------
  受注ルール一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      受注
      ログアウト
      } |
      {
        {
          {/ 一覧 | 一括登録 | <b>ルール}
          [ 確認 ] 
        }
      {+
        ---------------------
        {
          受注ルール | [削除]
          受注ルール | [削除]
          受注ルール | [削除]
        }
      }
     }
  }
}
@endsalt
`;

const uiModel = `
@startuml
    class 受注一覧 {
        検索()
        一括削除()
        詳細()
        削除()
    }
    class 受注 {
      登録()
    }
    class 受注一括登録 {
      アップロード()
      削除()
    }
    class アップロード {
        アップロード()
        キャンセル()
    }
    class 受注ルール一覧 {
      確認()
      削除()
    }
  
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    受注()
    ログアウト()
  }

  ナビゲーション --* 受注一覧
  ナビゲーション --* 受注一括登録
  ナビゲーション --* 受注ルール一覧
  受注一覧 *-- 受注
  受注一括登録 *-- アップロード
@enduml
`;

const uiInteraction = `
@startuml
  ログイン_シングル --> 受注_コレクション
    受注_コレクション --> 受注_シングル
    受注_シングル --> 受注_コレクション
    受注_コレクション --> 受注一括登録_コレクション
    受注一括登録_コレクション --> アップロード_シングル
    アップロード_シングル --> 受注一括登録_コレクション
    受注_コレクション --> 受注ルール_コレクション
  ログイン_シングル <-- 受注_コレクション  
  
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
