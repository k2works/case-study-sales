import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* マスタ管理
-- ユースケース
-- ドメインモデル
-- データモデル
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
- [ ] マスタ管理画面を作成する
`;

const usecase = `
@startuml
left to right direction
actor "管理者" as admin
rectangle マスタ管理 {
}
admin --> (マスタ管理)
@enduml
`;

const uml = `
@startuml
class マスタ一覧
class マスタ

マスタ一覧 *- マスタ
@enduml
`;

const erd = `
@startuml
' hide the spot
hide circle
' avoid problems with angled crows feet
skinparam linetype ortho
entity "マスタ" as master {
    *ID : text
    --
}
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
