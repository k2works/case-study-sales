import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* 非機能要件
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
## 非機能要件

- 楽観ロックによる排他制御
- アプリケーション実行履歴
- アプリケーションデータのダウンロード

## TODOリスト

`;

const usecase = `
@startuml
left to right direction
actor "管理者" as admin
rectangle 非機能要件 {
}

admin --> 非機能要件

@enduml
`;

const uml = `
@startuml
@enduml
`;

const erd = `
@startuml
@enduml
`;

const ui = `
@startsalt
@endsalt
`;

const uiModel = `
@startuml
@enduml
`;

const uiInteraction = `
@startuml
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
