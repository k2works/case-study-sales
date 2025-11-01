import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* 仕入/支払管理
-- ユースケース
--- 仕入管理
---- 仕入一覧
---- 仕入詳細
---- 仕入検索
---- 仕入編集
---- 仕入確認
---- 仕入ルール確認
--- 支払管理
---- 支払一覧
---- 支払詳細
---- 支払検索
---- 支払登録
---- 支払編集
---- 支払削除
---- 支払集計
-- ドメインモデル
--- 発注一覧
--- 発注
--- 発注明細
--- 発注ルール
--- 仕入一覧
--- 仕入
--- 仕入ルール
--- 仕入
--- 仕入明細
--- 仕入一覧
--- 仕入
--- 仕入明細
--- 商品
--- 仕入先
-- データモデル
--- 発注
--- 発注明細
--- 仕入
--- 仕入明細
--- 売上
--- 売上明細
--- 商品マスタ
--- 仕入マスタ
--- 自動採番マスタ
** ユーザーインターフェース
*** ビュー
**** ナビゲーション
**** 仕入一覧
**** 仕入
**** 仕入確認
**** 支払一覧
**** 支払
*** モデル
**** ナビゲーション
**** 仕入一覧
**** 仕入
**** 仕入確認
**** 支払一覧
**** 支払
*** インタラクション
**** ナビゲーション
**** 仕入一覧
**** 仕入
**** 仕入確認
**** 支払一覧
@endmindmap
`;

const contents = `
## ユーザーストーリー

- 管理者として仕入を管理できる
- 利用者として仕入を管理できる
- 管理者として支払を管理できる
- 利用者として支払を管理できる

## TODOリスト

- [x] 仕入APIを作成する
    - [x] 仕入データアクセスを実装する
    - [x] 仕入サービスを実装する
        - [x] 仕入一覧を実装する
        - [x] 仕入詳細を実装する
        - [x] 仕入検索を実装する
        - [x] 仕入編集を実装する
        - [x] 仕入確認を実装する
        - [x] 仕入ルール確認を実装する
    - [x] 仕入コントローラを実装する
        - [x] 仕入一覧を実装する
        - [x] 仕入詳細を実装する
        - [x] 仕入検索を実装する
        - [x] 仕入編集を実装する
        - [x] 仕入確認を実装する
        - [x] 仕入ルール確認を実装する
    - [x] 仕入ドメインロジックを実装する
- [x] 支払APIを作成する
    - [x] 支払データアクセスを実装する
    - [x] 支払サービスを実装する
        - [x] 支払一覧を実装する
        - [x] 支払詳細を実装する
        - [x] 支払検索を実装する
        - [x] 支払登録を実装する
        - [x] 支払編集を実装する
        - [x] 支払削除を実装する
        - [x] 支払集計を実装する
    - [x] 支払コントローラを実装する
        - [x] 支払一覧を実装する
        - [x] 支払詳細を実装する
        - [x] 支払検索を実装する
        - [x] 支払登録を実装する
        - [x] 支払編集を実装する
        - [x] 支払削除を実装する
        - [x] 支払集計を実装する
    - [x] 支払ドメインロジックを実装する
- [x] 仕入画面を作成する
    - [x] 仕入一覧画面を作成する
    - [x] 仕入詳細画面を作成する
    - [x] 仕入検索画面を作成する
    - [x] 仕入編集画面を作成する
    - [x] 仕入ルール確認画面を作成する
    - [x] 仕入確認画面を作成する
    - [x] E2Eテストを作成する
- [x] 支払画面を作成する
    - [x] 支払一覧画面を作成する
    - [x] 支払詳細画面を作成する
    - [x] 支払検索画面を作成する
    - [x] 支払登録画面を作成する
    - [x] 支払編集画面を作成する
    - [x] 支払集計画面を作成する
    - [x] E2Eテストを作成する

- [x] 実行履歴対応
    - [x] 仕入コントローラ
    - [x] 支払コントローラ
    
 - [x] ダウンロード機能
    - [x] 仕入データダウンロード
    - [x] 支払データダウンロード
`;

const usecase = `
@startuml
left to right direction
actor "利用者" as member
actor "管理者" as admin
rectangle 仕入管理 {
    usecase UC1 as "仕入一覧"
    usecase UC2 as "仕入詳細"
    usecase UC3 as "仕入検索"
    usecase UC4 as "仕入編集"
    usecase UC5 as "仕入確認"
    usecase UC6 as "仕入ルール確認"
}
rectangle 支払管理 {
    usecase UC7 as "支払一覧"
    usecase UC8 as "支払詳細"
    usecase UC9 as "支払検索"
    usecase UC10 as "支払登録"
    usecase UC11 as "支払編集"
    usecase UC12 as "支払削除"
    usecase UC13 as "支払集計"
}
admin --> UC1
admin --> UC2
admin --> UC3
admin --> UC4
admin --> UC5
admin --> UC6
admin --> UC7
admin --> UC8
admin --> UC9
admin --> UC10
admin --> UC11
admin --> UC12
admin --> UC13
member --> UC1
member --> UC2
member --> UC3
member --> UC4
member --> UC5
member --> UC6
member --> UC7
member --> UC8
member --> UC9
member --> UC10
member --> UC11
member --> UC12
member --> UC13
@enduml
`;

const uml = `
@startuml
class 発注一覧
class 発注
class 発注明細
class 発注ルール
class 仕入一覧
class 仕入
class 仕入明細
class 仕入ルール
class 支払一覧
class 支払
class 商品
class 仕入先

発注一覧 *-- 発注
発注 *-- 発注明細
発注ルール -> 発注
発注明細 <-- 仕入
仕入一覧 *-- 仕入
仕入 *-- 仕入明細
仕入ルール -> 仕入
支払一覧 *-- 支払
仕入 --> 支払
発注明細 *- 商品
仕入明細 *- 商品
仕入 *- 仕入先
発注 *- 仕入先
@enduml
`;

const erd = `
@startuml
' hide the spot
hide circle
' avoid problems with angled crows feet
skinparam linetype ortho

' 左側のエンティティ
together {
    entity "発注" as purchase_order {
        *発注ID : text
        --
        発注日 : date
        発注金額 : number
        仕入先ID : text
    }
    entity "発注明細" as purchase_order_detail {
        *発注ID : text
        *発注明細ID : text
        --
        商品ID : text
        数量 : number
        仕入指示数 : number
        仕入済数 : number
    }
}

' 中央のマスタテーブル
together {
    entity "仕入先マスタ" as supplier_master {
        *仕入先ID : text
        --
        仕入先名 : text
        仕入先コード : text
    }
    entity "商品マスタ" as product_master {
        *商品ID : text
        --
        商品名 : text
        単価 : number
    }
    entity "部門マスタ" as department_master {
        *部門ID : text
        --
        部門名 : text
        部門コード : text
    }
}

' 右側のエンティティ
together {
    entity "仕入" as purchase {
        *仕入ID : text
        --
        仕入日 : date
        仕入金額 : number
        仕入先ID : text
    }
    entity "仕入明細" as purchase_detail {
        *仕入ID : text
        *仕入明細ID : text
        --
        商品ID : text
        数量 : number
    }
    entity "支払" as payment {
        *支払ID : text
        --
        支払日 : date
        支払金額 : number
        部門ID : text
    }
}

entity "自動採番マスタ" as auto_number_master {
    *種別 : text
    *年月 : date
    --
    自動採番値 : number
}

' レイアウトヒント（仕入先マスタを商品マスタの上に配置）
supplier_master -[hidden]down-> product_master

' リレーション
purchase_order ||--o{ purchase_order_detail
purchase_order ||-o{ purchase
purchase ||--o{ purchase_detail
purchase ||-o{ payment
purchase_order_detail }o-|| product_master
purchase_detail }o-|| product_master
purchase_order }o-|| supplier_master
purchase }o-|| supplier_master
supplier_master ||--o{ product_master
department_master ||--o{ payment
@enduml
`;

const ui = `
@startsalt
{+
  仕入一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      発注
      ログアウト
      } |
      {
        {
          {/ <b>一覧 | ルール }
          [ 検索 ]
        }
      {+
        ---------------------
        {
          1xxxxxx | 商品1    | [  編集  ]
          1xxxxxx | 商品2    | [  編集  ]
          1xxxxxx | 商品3    | [  編集  ]
        }
      }
     }
  }
  ----------------
  仕入画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        発注番号    | "1xxxxxx"   "
        商品名        | "商品1    "
        数量        | "1"
        仕入指示数  | "1"
        仕入済数    | "0"
      }
  }
  ----------------
  仕入ルール画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      発注
      ログアウト
      } |
      {
        {
          {/ 一覧 | <b>ルール }
          [ 確認 ]
        }
      {+
        ---------------------
        {
          仕入ルール | [削除]
          仕入ルール | [削除]
          仕入ルール | [削除]
        }
      }
     }
  }
  ----------------
  支払一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      発注
      ログアウト
      } |
      {
        {
          {/ <b>一覧 | 支払集計 }
          [ 検索 ] | [  新規登録  ]
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
    支払画面（シングル）
    {+
        [保存]
        ---------------------
        {
          支払番号    | "1xxxxxx"   "
          商品名        | "商品1    "
          数量        | "1"
          支払金額    | "1000"
        }
    }
    ----------------
  支払集計画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      発注
      ログアウト
      } |
      {
        {
          {/ 一覧 | <b>支払集計 }
          [ 集計 ]
        }
      {+
      }
     }
  }
}
@endsalt
`;

const uiModel = `
@startuml
    class 仕入一覧 {
        検索()
        編集()
    }
    class 仕入 {
        保存()
    }
    class 仕入ルール {
        削除()
    }
    class 支払一覧 {
        検索()
        新規登録()
        編集()
        削除()
    }
    class 支払 {
        保存()
    }
    class 支払集計 {
        集計()
    }
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    仕入()
    支払()
    ログアウト()
  }

    ナビゲーション --* 仕入一覧
    仕入一覧 *-- 仕入
    ナビゲーション --* 仕入ルール
    ナビゲーション --* 支払一覧
    支払一覧 *-- 支払
    ナビゲーション --* 支払集計
@enduml
`;

const uiInteraction = `
@startuml
    ログイン_シングル --> 仕入一覧_コレクション
    仕入一覧_コレクション --> 仕入_シングル
    仕入_シングル --> 仕入一覧_コレクション
    ログイン_シングル --> 仕入確認_コレクション
    ログイン_シングル --> 支払一覧_コレクション
    支払一覧_コレクション --> 支払_シングル
    支払_シングル --> 支払一覧_コレクション
    ログイン_シングル --> 支払集計_コレクション
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
