import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* 回収管理
-- ユースケース
--- 回収
---- 入金一覧
---- 入金詳細
---- 入金検索
---- 入金登録
---- 入金編集
---- 入金削除
--- マスタ
---- 入金口座一覧
---- 入金口座詳細
---- 入金口座登録
---- 入金口座編集
---- 入金口座削除
-- ドメインモデル
--- 入金一覧
--- 入金
--- 入金口座
--- 部門
--- 売上一覧
--- 売上
--- 売上明細
--- 請求一覧
--- 請求
--- 請求明細
--- 顧客一覧
--- 顧客
-- データモデル
--- 入金データ
--- 入金口座マスタ
--- 部門マスタ
--- 顧客マスタ
--- 請求データ
--- 請求データ明細
--- 売上データ
--- 売上データ明細
** ユーザーインターフェース
*** ビュー
**** ナビゲーション
**** 入金一覧
**** 入金
*** モデル
**** ナビゲーション
**** 入金一覧
**** 入金
**** 入金口座一覧
**** 入金口座
*** インタラクション
**** ナビゲーション
**** 入金一覧
**** 入金
**** 入金口座一覧
**** 入金口座
@endmindmap
`;

const contents = `
## 請求管理

- 管理者として入金を管理できる
- 管理者として入金口座を管理できる
- ユーザーとして入金を管理できる

## TODOリスト

- [ ] 入金口座APIを実装する
    - [ ] 入金口座データアクセスを実装する
    - [ ] 入金口座サービスを実装する
        - [ ] 入金口座一覧を実装する
        - [ ] 入金口座詳細を実装する
        - [ ] 入金口座登録を実装する
        - [ ] 入金口座編集を実装する
        - [ ] 入金口座削除を実装する
    - [ ] 入金口座コントローラを実装する
        - [ ] 入金口座一覧を実装する
        - [ ] 入金口座詳細を実装する
        - [ ] 入金口座登録を実装する
        - [ ] 入金口座編集を実装する
        - [ ] 入金口座削除を実装する
    - [ ] 入金口座ドメインロジックを実装する
- [ ] 入金APIを実装する
  - [ ] 入金データアクセスを実装する
  - [ ] 入金サービスを実装する
      - [ ] 入金一覧を実装する
      - [ ] 入金詳細を実装する
      - [ ] 入金検索を実装する
      - [ ] 入金登録を実装する
      - [ ] 入金編集を実装する
      - [ ] 入金削除を実装する
      - [ ] 入金集計を実装する
  - [ ] 入金コントローラを実装する
      - [ ] 入金一覧を実装する
      - [ ] 入金詳細を実装する
      - [ ] 入金検索を実装する
      - [ ] 入金登録を実装する
      - [ ] 入金編集を実装する
      - [ ] 入金削除を実装する
      - [ ] 入金集計を実装する
  - [ ] 入金ドメインロジックを実装する
- [ ] 入金口座UIを実装する
    - [ ] 入金口座一覧画面を実装する
    - [ ] 入金口座詳細画面を実装する
    - [ ] 入金口座検索画面を実装する
    - [ ] 入金口座登録画面を実装する
    - [ ] 入金口座編集画面を実装する
    - [ ] 入金口座削除画面を実装する
- [ ] 入金UIを実装する
   - [ ] 入金一覧画面を実装する
   - [ ] 入金詳細画面を実装する
   - [ ] 入金検索画面を実装する
   - [ ] 入金登録画面を実装する
   - [ ] 入金編集画面を実装する
   - [ ] 入金削除画面を実装する
`;

const usecase = `
@startuml
left to right direction
actor "ユーザー" as member
actor "管理者" as admin

rectangle 入金管理 {
    usecase UC1 as "入金一覧"
    usecase UC2 as "入金詳細"
    usecase UC3 as "入金検索"
    usecase UC4 as "入金登録"
    usecase UC5 as "入金編集"
    usecase UC6 as "入金削除"
    usecase UC7 as "入金集計"
}

rectangle マスタ {
    usecase UC8 as "入金口座一覧"
    usecase UC9 as "入金口座詳細"
    usecase UC10 as "入金口座登録"
    usecase UC11 as "入金口座編集"
    usecase UC12 as "入金口座削除"
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
class 入金一覧
class 入金
class 入金口座
class 部門
class 売上一覧
class 売上
class 売上明細
class 顧客一覧
class 顧客
class 請求一覧
class 請求
class 請求明細

入金一覧 *- 入金
入金 *-- 入金口座
入金 *-- 部門
顧客 --* 入金
入金口座 *- 部門
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
entity "入金データ" as payment {
    *入金ID : text
    --
    入金日 : date
    入金金額 : number
    入金口座ID : text
    部門ID : text
    顧客ID : text
}
entity "入金口座マスタ" as payment_account {
    *入金口座ID : text
    --
    口座名 : text
    銀行名 : text
    支店名 : text
    口座番号 : text
    口座種別 : text
}
entity "部門マスタ" as department {
    *部門ID : text
    --
    部門名 : text
    部門コード : text
    説明 : text
}
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

payment ||--o{ payment_account
payment ||--o{ department
payment ||--o{ customer_master
payment_account ||-o{ department
sales ||--o{ sales_detail
invoice ||--o{ invoice_detail
customer_master ||-o{ invoice
invoice_detail ||-o{ sales_detail

@enduml
`;

const ui = `
@startsalt
{+
  入金一覧画面（コレクション）
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
          {/ <b>一覧 }
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
    入金画面（シングル）
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
    入金口座一覧画面（コレクション）
    {+
        {
          {/ <b>一覧 }
          [ 検索 ] | [  新規登録  ] 
        }
      {+
        ---------------------
        {
          1xxxxxx | 口座名1    | [  編集  ] | [  削除  ]
          1xxxxxx | 口座名2    | [  編集  ] | [  削除  ]
          1xxxxxx | 口座名3    | [  編集  ] | [  削除  ]
        }
      }
    }
    ----------------
    入金口座画面（シングル）
    {+
        [保存]
        ---------------------
        {
            入金口座ID : 1xxxxxx
            口座名 : 口座名1
            銀行名 : 銀行名1
            支店名 : 支店名1
            口座番号 : 1234567
            口座種別 : 普通
        }
    }
    
}
@endsalt
`;

const uiModel = `
@startuml
    class 入金一覧 {
        検索()
        新規登録()
        編集()
        削除()
    }
    class 入金 {
        保存()
    }
    class 入金口座一覧 {
        検索()
        新規登録()
        編集()
        削除()
    }
    class 入金口座 {
        保存()
    }
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    入金()
    ログアウト()
  }

    ナビゲーション --* 入金一覧
    入金一覧 *-- 入金
    ナビゲーション --* 入金口座一覧
    入金口座一覧 *-- 入金口座
@enduml
`;

const uiInteraction = `
@startuml
    ログイン_シングル --> 入金一覧_コレクション
    入金一覧_コレクション --> 入金_シングル
    入金_シングル --> 入金一覧_コレクション
    ログイン_シングル --> 入金口座一覧_コレクション
    入金口座一覧_コレクション --> 入金口座_シングル
    入金口座_シングル --> 入金口座一覧_コレクション
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });
