import "./style.css";
import render from "@k2works/full-stack-lab";

const contents = `
## 認証機能

## 仕様

- ユーザーIDとパスワードで認証されたユーザーはホーム画面に遷移する
- 登録されていないユーザーは認証されない
- 管理者権限を持つユーザーは管理者として認証される

## TODOリスト
- [x] ユーザーDBを作成する
- [x] 認証APIを作成する
- [x] ログイン画面を作成する
  - npm install react-router-dom react-modal react-spinner react-tab react-icons
- [ ] ホーム画面を作成する
`;

const mindmap = `
@startmindmap
* 認証
-- ユースケース
--- ユーザーを認証する
--- 管理者を認証する
-- オブジェクトモデル
--- 認証APIを作成する
-- データモデル
--- ユーザーDBを作成する
** ユーザーインターフェース
*** ログイン画面を作成する
*** ホーム画面を作成する
** モデル
** インタラクション
@endmindmap
`;

const usecase = `
@startuml
left to right direction
actor "ユーザー" as member
actor "管理者" as admin
rectangle ユーザー認証 {
  usecase "管理者を認証する" as UC1
  usecase "ユーザーを認証する" as UC2
}
admin --> UC1
member --> UC2
@enduml
`;

const ui = `
@startsalt
{+
  ログイン画面
  {+
    {
      [  ログイン   ]
      ---------------------
      ユーザーID    | "MyName   "
      パスワード | "****     "
    }
  }
----------------
  ホーム画面
  {+
  {
  ホーム
  ログアウト
  } |
  {
    {
      <b>お知らせ
    }
    ---------------------
    {
        2020/01/01 お知らせ内容
        2020/01/02 お知らせ内容
        2020/01/03 お知らせ内容
    }
  }
  }
}
@endsalt
`;

const uiModel = `
@startuml
  class ログイン {
    ユーザーID
    パスワード
    ログイン()
  }
  
  class ホーム {
    お知らせ
  }
  
  class ナビゲーション {
    ホーム()
    ログアウト()
  }
  
  ナビゲーション -* ホーム 
`;

const uiInteraction = `
@startuml
  ログイン_シングル --> ホーム_シングル
  ホーム_シングル --> ログイン_シングル 
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
