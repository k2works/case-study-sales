import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* マスタ管理
-- ユースケース
--- マスタ管理
---- 部門
----- 部門一覧
----- 部門詳細
----- 部門新規作成
----- 部門編集
----- 部門削除
---- 社員
----- 社員一覧
----- 社員詳細
----- 社員新規作成
----- 社員編集
----- 社員削除
-- ドメインモデル
--- 部門
--- 社員
-- データモデル
--- 部門マスタ
--- 社員マスタ
** ユーザーインターフェース
*** ビュー
**** 部門一覧
**** 部門
***** 社員一覧選択
**** 社員一覧
**** 社員
***** 部門一覧選択
***** ユーザー一覧選択
*** モデル
**** 部門一覧
**** 部門
**** 社員一覧選択
**** 社員
**** 部門一覧選択
**** ユーザー一覧選択
*** インタラクション
**** ログイン
**** ナビゲーション
***** 部門
***** 社員
***** ユーザー
**** ログアウト

@endmindmap
`;

const contents = `
## マスタ管理機能

## 仕様

- 管理者はマスタを管理できる

## TODOリスト

- [ ] マスタ管理APIを作成する
  - [ ] 部門APIを作成する
    - [x] 部門一覧が全件selectされない
    - [x] 部門の開始日がずれて更新と登録がうまくいかない
  - [ ] 社員APIを作成する
- [ ] マスタ管理画面を作成する
  - [ ] 部門画面を作成する
    - [x] 部門に所属する社員を表示する
    - [x] 社員一覧から社員を追加する
  - [ ] 社員画面を作成する
    - [x] 部門一覧から部門を選択する
    - [x] ユーザー一覧からユーザーを選択する
- [ ] 初期データの日付がUTCなのでJSTに変換する
- [x] 削除実行時に確認ダイアログを表示する
- [x] 一括削除機能を追加する
- [x] uiからviewsに名前変更
- [x] typesからmodelsに名前変更
`;

const usecase = `
@startuml
left to right direction
actor "管理者" as admin
rectangle マスタ管理 {
 rectangle 部門 {
    (部門一覧)
    (部門詳細)
    (部門新規作成)
    (部門編集)
    (部門削除)
 }
 rectangle 社員 {
    (社員一覧)
    (社員詳細)
    (社員新規作成)
    (社員編集)
    (社員削除)
 }
}
admin --> (部門一覧)
admin --> (部門詳細)
admin --> (部門新規作成)
admin --> (部門編集)
admin --> (部門削除)
admin --> (社員一覧)
admin --> (社員詳細)
admin --> (社員新規作成)
admin --> (社員編集)
admin --> (社員削除)

@enduml
`;

const uml = `
@startuml
class 部門一覧
class 部門

class 社員一覧
class 社員
class ユーザー

部門一覧 *-- 部門
部門 *-- 社員
社員一覧 *-- 社員
社員 o- ユーザー
@enduml
`;

const erd = `
@startuml
' hide the spot
hide circle
' avoid problems with angled crows feet
skinparam linetype ortho
entity "部門マスタ" as department {
    *部門コード : text
    --
    *開始日 : date
    終了日 : date
    部門名 : text
}

entity "社員マスタ" as employee {
    *社員コード : text
    --
    社員名 : text
    部門コード : text
    開始日 : date
    ユーザーID : text
}

entity "ユーザーマスタ" as user {
    *ユーザーID : text
    --
    ユーザー名 : text
    パスワード : text
    役割 : text
}

department ||--o{ employee
employee ||-o user
@enduml
`;

const ui = `
@startsalt
{+
  部門画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          <b>部門一覧</b>
        }
        
        [  新規登録  ] | [ 一括削除  ]
        ---------------------
        {
          1xxxxxx | 部門1    | [  編集  ] | [  削除  ]
          1xxxxxx | 部門2    | [  編集  ] | [  削除  ]
          1xxxxxx | 部門3    | [  編集  ] | [  削除  ]
        }
      } 
  }
  ----------------
  部門画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        部門コード    | "1xxxxxx"   "
        部門名        | "部門1    "
        開始日        | "2021/01/01"
        終了日        | "9999/12/31"
        最下層区分  | () Yes   () No        
      }
  
     <b>社員一覧</b>
     [  追加  ]
    ---------------------
    {
      EMPxxx | 社員1   | [  削除  ]
      EMPxxx | 社員2   | [  削除  ]
      EMPxxx | 社員3   | [  削除  ]
    }
  }
  ----------------
  社員一覧選択画面（コレクション）
  {+
    {
      <b>社員一覧</b>
    }
    ---------------------
    {
      EMPxxx | 社員1   | [  選択  ]
      EMPxxx | 社員2   | [  選択  ]
      EMPxxx | 社員3   | [  選択  ]
    }
  }
  ----------------
  社員一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ 
      ログアウト
      } |
      {
        {
          <b>社員一覧</b>
        }
        [  新規登録  ] | [ 一括削除  ]
        ---------------------
        {
          EMPxxx | 社員1    | [  編集  ] | [  削除  ]
          EMPxxx | 社員2    | [  編集  ] | [  削除  ]
          EMPxxx | 社員3    | [  編集  ] | [  削除  ]
        }
      } 
  }
  ----------------
  社員画面（シングル）
  {+
        {
        [  保存   ] | [ 部門一覧 ] | [ ユーザー一覧 ]
        ---------------------
        社員コード    | "EMPxxx"   "
        姓           | "MyName   "
        名           | "MyName   "
        部門          | "部門1    "
        ユーザー      | "user1    "
        }
  }
  ----------------
  部門一覧選択画面（コレクション）
  {+
      {
        {
          <b>部門一覧</b>
        }
        ---------------------
        {
          1xxxxxx | 部門1    | [  選択  ] 
          1xxxxxx | 部門2    | [  選択  ]
          1xxxxxx | 部門3    | [  選択  ]
        }
      } 
  }
  ----------------
  ユーザー一覧選択画面（コレクション）
  {+
    {
      <b>ユーザー一覧</b>
    }
    ---------------------
    {
      Uxxxxxx | User1    | [ 選択  ]
      Uxxxxxx | User2    | [ 選択  ]
      Uxxxxxx | User3    | [ 選択  ]
    }
  }
}
@endsalt
`;

const uiModel = `
@startuml
  class 部門一覧 {
    新規作成()
    編集()
    削除()
    一括削除()
  }

  class 部門 {
    保存()
    追加()
    削除()
  }
  
  class 社員一覧選択 {
     選択()
  }
  
  class 社員一覧 {
    新規作成()
    編集()
    削除()
    一括削除()
  }
  
  class 社員 {
    保存()
  }
  
  class 部門一覧選択 {
    選択()
  }
  
  class ユーザー一覧選択 {
    選択()
  }
  
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    ログアウト()
  }
  
  ナビゲーション --* 部門一覧
  部門一覧 *-- 部門
  部門 *-- 社員一覧選択
  ナビゲーション --* 社員一覧
  社員一覧 *-- 社員
  社員 *-- ユーザー一覧選択
  社員 *-- 部門一覧選択
@enduml
`;

const uiInteraction = `
@startuml

  ログイン_シングル --> ナビゲーション_シングル
    ナビゲーション_シングル --> 部門_コレクション
    部門_コレクション --> 部門_シングル
    部門_シングル --> 部門_コレクション
    部門_シングル --> 社員一覧選択_コレクション
    社員一覧選択_コレクション --> 部門_シングル 
    
    ナビゲーション_シングル --> 社員_コレクション
    社員_コレクション --> 社員_シングル
    社員_シングル --> 社員_コレクション
    社員_シングル --> 部門一覧選択_コレクション
    部門一覧選択_コレクション --> 社員_シングル
    社員_シングル --> ユーザー一覧選択_コレクション
    ユーザー一覧選択_コレクション --> 社員_シングル
  ログイン_シングル <-- ナビゲーション_シングル
  
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
