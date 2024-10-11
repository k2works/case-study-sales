import "./style.css";
import render from "@k2works/full-stack-lab";

const contents = `
## ユーザー管理機能

## 仕様

- 管理者はユーザーを管理できる

## TODOリスト
- [x] ユーザー管理APIを作成する
- [ ] ユーザー管理画面を作成する
`;

const mindmap = `
@startmindmap
* ユーザー管理
-- ユースケース
--- ユーザー一覧を取得する
--- ユーザーを新規登録する
--- ユーザーを取得する
--- 登録済みユーザーを更新登録する
--- 登録済みユーザーを削除する
-- オブジェクトモデル
--- ユーザー管理APIを作成する
-- データモデル
--- ユーザーDBを作成する
** ユーザーインターフェース
*** ユーザー管理画面を作成する
** モデル
*** ログイン
*** ナビゲーション
*** ユーザー一覧
**** ユーザー
** インタラクション
@endmindmap
`;

const usecase = `
@startuml
left to right direction
actor "管理者" as admin
rectangle ユーザー管理 {
    usecase "ユーザー一覧を取得する" as UC1
    usecase "ユーザーを新規登録する" as UC2
    usecase "ユーザーを取得する" as UC3
    usecase "登録済みユーザーを更新登録する" as UC4
    usecase "登録済みユーザーを削除する" as UC5
}
admin --> UC1
admin --> UC2
admin --> UC3
admin --> UC4
admin --> UC5
@enduml
`;

const ui = `
@startsalt
{+
  ユーザー管理画面（コレクション）
  {+
  {
  ホーム
  ユーザー
  ログアウト
  } |
  {
    {
      <b>ユーザー一覧</b>
    }
    [  新規登録  ]
    ---------------------
    {
      Uxxxxxx | User1    | [  編集  ] | [  削除  ]
      Uxxxxxx | User2    | [  編集  ] | [  削除  ]
      Uxxxxxx | User3    | [  編集  ] | [  削除  ]
    }
  }
  }
  ----------------
    ユーザー管理画面（シングル）
    {+
        {
        [  保存   ]
        ---------------------
        ユーザーID    | "Uxxxxxx"   "
        姓           | "MyName   "
        名           | "MyName   "
        パスワード | "****     "
        役割       | ^Admin     ^^User    ^
        }
    }
}
@endsalt
`;

const uiModel = `
@startuml
  class ユーザー一覧 {
  }
  
  class ユーザー {
    ユーザーID
    パスワード
    役割
    名前   
  }
  
  class ナビゲーション {
    ホーム()
    ユーザー()
    ログアウト()
  }
  
  ユーザー一覧 *-- ユーザー
  ナビゲーション -* ユーザー一覧
`;

const uiInteraction = `
@startuml
  ログイン_シングル --> ユーザー_コレクション
    ユーザー_コレクション --> ユーザー_シングル
    ユーザー_シングル --> ユーザー_コレクション
  ログイン_シングル <-- ユーザー_コレクション
@enduml
`;

const uml = `
@startuml
class ユーザー一覧
class ユーザー
class ユーザーID
class パスワード
class 役割
class 名前 {
    + 姓
    + 名
}

ユーザー一覧 *- ユーザー
ユーザー *-- ユーザーID
ユーザー *-- パスワード
ユーザー *-- 役割
ユーザー *-- 名前
@enduml
`;

const erd = `
@startuml
' hide the spot
hide circle
' avoid problems with angled crows feet
skinparam linetype ortho
entity "ユーザー" as usr {
    *ユーザーID : text
    --
    姓 : text
    名 : text
    パスワード : text
    役割 : text
}
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
