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
*** モデル
*** インタラクション
@endmindmap
`;

const contents = `
## マスタ管理機能

## 仕様

- 管理者はマスタを管理できる

## TODOリスト
- [ ] マスタ管理APIを作成する
  - [ ] 部門APIを作成する
    - [ ] 部門一覧が全件selectされない
    - [ ] 部門の開始日がずれて更新と登録がうまくいかない
  - [ ] 社員APIを作成する
- [ ] マスタ管理画面を作成する
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

部門一覧 *-- 部門
部門 *-- 社員
社員一覧 *-- 社員
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
}

department ||--o{ employee
@enduml
`;

const ui = `
@startsalt
{+
  マスタ管理画面（コレクション）
  {+
  }
  ----------------
  マスタ管理画面（シングル）
  {+
  }
}
@endsalt
`;

const uiModel = `
@startuml
  class マスタ一覧 {
    新規作成()
    編集()
    削除()
  }

  class マスタ {
    保存()
  }
  
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    ログアウト()
  }
  
  ユーザー一覧 *-- マスタ
  ナビゲーション -* マスタ一覧
@enduml
`;

const uiInteraction = `
@startuml
  ログイン_シングル --> マスタ_コレクション
    マスタ_コレクション --> マスタ_シングル
    マスタ_シングル --> マスタ_コレクション
  ログイン_シングル <-- マスタ_コレクション
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
