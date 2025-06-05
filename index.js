import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* 請求管理
-- ユースケース
--- 請求管理
---- 請求一覧
---- 請求詳細
---- 請求検索
---- 請求登録
---- 請求編集
---- 請求削除
---- 請求集計
-- ドメインモデル
--- 売上一覧
--- 売上
--- 売上明細
--- 請求一覧
--- 請求
--- 請求明細
--- 顧客一覧
--- 顧客
-- データモデル
--- 顧客マスタ
--- 請求データ
--- 請求データ明細
--- 売上データ
--- 売上データ明細
** ユーザーインターフェース
*** ビュー
**** ナビゲーション
**** 請求一覧
**** 請求
*** モデル
**** ナビゲーション
**** 請求一覧
**** 請求
*** インタラクション
**** ナビゲーション
**** 請求一覧
**** 請求
@endmindmap
`;

const contents = `
## 請求管理

- 管理者として請求を管理できる
- ユーザーとして請求を管理できる

## TODOリスト

- [x] 請求APIを実装する
  - [x] 請求データアクセスを実装する
  - [x] 請求サービスを実装する
    - [x] 請求一覧を実装する
    - [x] 請求詳細を実装する
    - [x] 請求検索を実装する
    - [x] 請求登録を実装する
    - [x] 請求編集を実装する
    - [x] 請求削除を実装する
    - [x] 請求集計を実装する
  - [x] 請求コントローラを実装する
    - [x] 請求一覧を実装する
    - [x] 請求詳細を実装する
    - [x] 請求検索を実装する
    - [x] 請求登録を実装する
    - [x] 請求編集を実装する
    - [x] 請求削除を実装する
    - [x] 請求集計を実装する
    - [x] 実行履歴対応
  - [x] 請求ドメインロジックを実装する   
- [ ] 請求UIを実装する
  - [ ] 請求一覧画面を実装する
  - [ ] 請求詳細画面を実装する
  - [ ] 請求検索画面を実装する
  - [ ] 請求登録画面を実装する
  - [ ] 請求編集画面を実装する 

`;

const usecase = `
@startuml
left to right direction
actor "ユーザー" as member
actor "管理者" as admin

rectangle 請求管理 {
    usecase UC1 as "請求一覧"
    usecase UC2 as "請求詳細"
    usecase UC3 as "請求検索"
    usecase UC4 as "請求登録"
    usecase UC5 as "請求編集"
    usecase UC6 as "請求削除"
    usecase UC7 as "請求集計"
}
admin --> UC1
admin --> UC2
admin --> UC3
admin --> UC4
admin --> UC5
admin --> UC6
admin --> UC7

member --> UC1
member --> UC2
member --> UC3
member --> UC4
member --> UC5
member --> UC6
member --> UC7
@enduml
`;

const uml = `
@startuml
class 売上一覧
class 売上
class 売上明細
class 顧客一覧
class 顧客
class 請求一覧
class 請求
class 請求明細

売上一覧 *-- 売上
売上 *-- 売上明細
顧客一覧 *-- 顧客
請求一覧 *-- 請求
請求 *-- 請求明細
顧客 *- 請求
請求明細 *- 売上明細
@enduml
`;

const erd = `
@startuml
' hide the spot
hide circle
' avoid problems with angled crows feet
skinparam linetype ortho
entity "顧客マスタ" as customer_master {
    *顧客ID : text
    --
    顧客名 : text
    住所 : text
    電話番号 : text
    メールアドレス : text
    登録日 : date
    更新日 : date
}
entity "請求データ" as invoice {
    *請求ID : text
    --
    請求日 : date
    請求金額 : number
    顧客ID : text
}
entity "請求データ明細" as invoice_detail {
    *請求ID : text
    *請求明細ID : text
    --
    商品ID : text
    数量 : number
    単価 : number
    金額 : number
}
entity "売上" as sales {
    *売上ID : text
    --
    売上日 : date
    売上金額 : number
}
entity "売上明細" as sales_detail {
    *売上ID : text
    *売上明細ID : text
    --
    商品ID : text
    数量 : number
}

sales ||--o{ sales_detail
invoice ||--o{ invoice_detail
customer_master ||-o{ invoice
invoice_detail ||-o{ sales_detail

@enduml
`;

const ui = `
@startsalt
{+
  請求一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      販売 
      ログアウト
      } |
      {
        {
          {/ <b>一覧 | 請求集計 }
          [ 検索 ] | [  新規登録  ] 
        }
      {+
        ---------------------
        {
          1xxxxxx | 取引先1    | [  編集  ] | [  削除  ]
          1xxxxxx | 取引先2    | [  編集  ] | [  削除  ]
          1xxxxxx | 取引先3    | [  編集  ] | [  削除  ]
        }
      }
     }
  }
    ----------------
    請求画面（シングル）
    {+
        [保存]
        ---------------------
        {
            請求ID : 1xxxxxx
            請求日 : 2023/10/01
            請求金額 : 100,000
            顧客ID : 1xxxxxx
        }
    } 
    ----------------
  請求集計画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      販売 
      ログアウト
      } |
      {
        {
          {/ 一覧 | <b>請求集計 }
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
    class 請求一覧 {
        検索()
        新規登録()
        編集()
        削除()
    }
    class 請求 {
        保存()
    }
    class 請求集計 {
        集計()
    }
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    請求()
    ログアウト()
  }

    ナビゲーション --* 請求一覧
    請求一覧 *-- 請求
    ナビゲーション --* 請求集計
@enduml
`;

const uiInteraction = `
@startuml
    ログイン_シングル --> 請求一覧_コレクション
    請求一覧_コレクション --> 請求_シングル
    請求_シングル --> 請求一覧_コレクション
    ログイン_シングル --> 請求集計_コレクション
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
