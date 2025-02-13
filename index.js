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
-- ドメインモデル
--- 受注一覧
--- 受注
--- 受注明細
-- データモデル
--- 受注
--- 受注明細
** ユーザーインターフェース
*** ビュー
**** ナビゲーション
**** 受注一覧
**** 受注
*** モデル
**** ナビゲーション
**** 受注一覧
**** 受注
*** インタラクション
**** ナビゲーション
**** 受注一覧
**** 受注

@endmindmap
`;

const contents = `
## 受注管理

- 管理者と利用者は受注を管理できる

## TODOリスト

- [ ] 受注APIを作成する
  - [ ] 受注一覧
  - [ ] 受注詳細
  - [ ] 受注検索
  - [ ] 受注登録
  - [ ] 受注編集
  - [ ] 受注削除
- [ ] 受注UIを作成する
  - [ ] 受注一覧
  - [ ] 受注詳細

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
}

admin --> 受注一覧
admin --> 受注詳細
admin --> 受注検索
admin --> 受注登録
admin --> 受注編集
admin --> 受注削除
user --> 受注一覧
user --> 受注詳細
user --> 受注検索
user --> 受注登録
user --> 受注編集
user --> 受注削除

@enduml
`;

const uml = `
@startuml
class 受注一覧
class 受注
class 受注明細

受注一覧 *-- 受注
受注 *- 受注明細

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


受注 ||--o{ 受注明細
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
      ログアウト
      } |
      {
        {
          {/ <b>登録 | }
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
      アップロード()
    }
  
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    ログアウト()
  }

  ナビゲーション --* 受注一覧
  受注一覧 *-- 受注
@enduml
`;

const uiInteraction = `
@startuml
  ログイン_シングル --> 受注_コレクション
    受注_コレクション --> 受注_シングル
    受注_シングル --> 受注_コレクション
  ログイン_シングル <-- 受注_コレクション  
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
