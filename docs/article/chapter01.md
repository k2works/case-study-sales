# 第1章: プロジェクト概要

## 1.1 販売管理システムとは

### 販売管理システムの業務概要

販売管理システム（Sales Management System）とは、企業の販売活動を効率的に管理するための情報システムです。商品の受注から出荷、売上計上、請求、入金管理に至るまでの一連の販売プロセスを一元的に管理し、業務効率化と経営判断の迅速化を支援します。

```plantuml
@startuml
title 販売管理システムの業務範囲

rectangle "販売管理システム" {
  rectangle "販売" {
    (受注) --> (出荷)
    (出荷) --> (売上)
    (売上) --> (請求)
    (請求) --> (回収)
  }

  rectangle "調達" {
    (発注) --> (仕入)
    (仕入) --> (支払)
  }

  rectangle "在庫" {
    (在庫管理)
  }
}

(出荷) --> (在庫管理) : 出庫
(仕入) --> (在庫管理) : 入庫

@enduml
```

### 販売・調達・在庫の3つの柱

本システムは、企業活動の根幹をなす3つの主要業務領域をカバーしています。

#### 販売管理

販売管理は、顧客からの受注を起点として、出荷・売上・請求・回収までの一連の流れを管理する領域です。

```plantuml
@startuml
title 販売プロセス

[*] --> 受注
受注 : 顧客からの注文を受け付ける
受注 --> 出荷指示 : 受注確定
出荷指示 : 倉庫への出荷指示を発行
出荷指示 --> 出荷 : 出荷準備完了
出荷 : 商品を顧客へ発送
出荷 --> 売上計上 : 出荷完了
売上計上 : 売上として計上
売上計上 --> 請求 : 締め処理
請求 : 顧客へ請求書を発行
請求 --> 入金 : 入金確認
入金 : 入金を消し込み
入金 --> [*]

@enduml
```

- **受注管理**: 顧客からの注文情報を登録・管理し、納期や数量の調整を行います
- **出荷管理**: 受注に基づいて出荷指示を作成し、倉庫からの出荷を管理します
- **売上管理**: 出荷完了後の売上計上と売上データの管理を行います
- **請求管理**: 締め日に基づいて請求書を作成し、顧客への請求を管理します
- **回収管理**: 入金情報を登録し、売掛金の消込処理を行います

#### 調達管理

調達管理は、仕入先への発注から仕入・支払までの一連の流れを管理する領域です。

```plantuml
@startuml
title 調達プロセス

[*] --> 発注
発注 : 仕入先へ注文を発行
発注 --> 入荷 : 発注送信
入荷 : 商品の入荷を確認
入荷 --> 仕入計上 : 検収完了
仕入計上 : 仕入として計上
仕入計上 --> 支払 : 締め処理
支払 : 仕入先へ支払を実行
支払 --> [*]

@enduml
```

- **発注管理**: 仕入先への発注情報を作成・管理し、発注ステータスを追跡します
- **仕入管理**: 入荷確認と仕入計上を管理します
- **支払管理**: 締め日に基づいて支払予定を作成し、支払処理を管理します

#### 在庫管理

在庫管理は、商品の入出庫を追跡し、適切な在庫水準を維持するための領域です。

```plantuml
@startuml
title 在庫管理

rectangle "在庫管理" {
  (入庫) --> (在庫)
  (在庫) --> (出庫)
  (在庫) --> (棚卸)
}

(仕入) --> (入庫)
(出荷) --> (出庫)

@enduml
```

- **倉庫管理**: 商品を保管する倉庫の情報を管理します
- **棚番管理**: 倉庫内のロケーション（棚番）を管理します
- **在庫数量管理**: 商品ごとの在庫数量をリアルタイムで把握します

### ビジネス要件の整理

本システムが満たすべきビジネス要件を以下に整理します。

#### 機能要件

| カテゴリ | 要件 |
|---------|------|
| マスタ管理 | 部門、社員、商品、取引先の情報を管理できる |
| 販売管理 | 受注から回収までの販売プロセスを一元管理できる |
| 調達管理 | 発注から支払までの調達プロセスを一元管理できる |
| 在庫管理 | 在庫の入出庫と現在庫を正確に把握できる |
| ユーザー管理 | ロールベースのアクセス制御ができる |
| 監査 | 操作履歴を記録・参照できる |

#### 非機能要件

| カテゴリ | 要件 |
|---------|------|
| 可用性 | 業務時間中は安定して稼働する |
| 性能 | 一覧表示は3秒以内に応答する |
| セキュリティ | 認証・認可により不正アクセスを防止する |
| 保守性 | 機能追加・変更が容易な構造とする |

---

## 1.2 システム全体像

### ユースケース概要図

本システムの主要なユースケースを以下に示します。

```plantuml
@startuml
left to right direction
actor "管理者" as admin
actor "利用者" as user
actor "システム" as system

rectangle "認証・ユーザー管理" {
  usecase "ログイン" as UC_LOGIN
  usecase "ユーザー管理" as UC_USER
}

rectangle "マスタ管理" {
  usecase "部門管理" as UC_DEPT
  usecase "社員管理" as UC_EMP
  usecase "商品管理" as UC_PROD
  usecase "取引先管理" as UC_PARTNER
}

rectangle "販売管理" {
  usecase "受注管理" as UC_ORDER
  usecase "出荷管理" as UC_SHIP
  usecase "売上管理" as UC_SALES
  usecase "請求管理" as UC_BILL
  usecase "回収管理" as UC_COLLECT
}

rectangle "調達管理" {
  usecase "発注管理" as UC_PO
  usecase "仕入管理" as UC_PURCHASE
  usecase "支払管理" as UC_PAY
}

rectangle "在庫管理" {
  usecase "在庫管理" as UC_INV
  usecase "倉庫管理" as UC_WH
  usecase "棚番管理" as UC_LOC
}

rectangle "システム管理" {
  usecase "監査ログ" as UC_AUDIT
  usecase "データダウンロード" as UC_DL
}

admin --> UC_LOGIN
admin --> UC_USER
admin --> UC_DEPT
admin --> UC_EMP
admin --> UC_PROD
admin --> UC_PARTNER
admin --> UC_ORDER
admin --> UC_SHIP
admin --> UC_SALES
admin --> UC_BILL
admin --> UC_COLLECT
admin --> UC_PO
admin --> UC_PURCHASE
admin --> UC_PAY
admin --> UC_INV
admin --> UC_WH
admin --> UC_LOC
admin --> UC_AUDIT
admin --> UC_DL

user --> UC_LOGIN
user --> UC_ORDER
user --> UC_SHIP
user --> UC_SALES
user --> UC_PO
user --> UC_PURCHASE
user --> UC_INV

system --> UC_AUDIT

@enduml
```

### 主要機能モジュール

#### 認証・ユーザー管理

システムへのアクセスを制御する認証機能と、ユーザー情報を管理する機能を提供します。

```plantuml
@startuml
package "認証・ユーザー管理" {
  class User {
    userId: UserId
    name: Name
    password: Password
    role: RoleName
  }

  class AuthUserDetails {
    user: User
    authorities: Collection<GrantedAuthority>
  }

  enum RoleName {
    ADMIN
    USER
  }

  User --> RoleName
  AuthUserDetails --> User
}
@enduml
```

- **ログイン/ログアウト**: JWT トークンによる認証
- **ユーザー CRUD**: ユーザーの登録・編集・削除
- **ロール管理**: 管理者・一般ユーザーのロール割当

#### マスタ管理

システムで使用する基本情報（マスタデータ）を管理します。

```plantuml
@startuml
package "マスタ管理" {
  class Department {
    departmentId: DepartmentId
    departmentCode: DepartmentCode
    departmentName: String
    path: DepartmentPath
    startDate: DepartmentStartDate
    endDate: DepartmentEndDate
  }

  class Employee {
    employeeCode: EmployeeCode
    employeeName: EmployeeName
    department: Department
    jobCode: JobCode
  }

  class Product {
    productCode: ProductCode
    productName: ProductName
    productType: ProductType
    sellingPrice: Money
    purchasePrice: Money
  }

  class Partner {
    partnerCode: PartnerCode
    partnerName: PartnerName
    partnerGroup: PartnerGroup
    credit: Credit
  }

  Employee --> Department
  Partner --> PartnerGroup
}
@enduml
```

- **部門管理**: 階層構造を持つ部門マスタの管理
- **社員管理**: 社員情報と部門割当の管理
- **商品管理**: 商品情報と顧客別販売単価の管理
- **取引先管理**: 顧客・仕入先を統合したパーティモデルによる管理

#### 販売（受注 → 出荷 → 売上 → 請求 → 回収）

販売プロセス全体を一元管理します。

```plantuml
@startuml
package "販売管理" {
  class SalesOrder {
    orderNumber: OrderNumber
    orderDate: OrderDate
    customer: Customer
    deliveryDate: DeliveryDate
    lines: List<SalesOrderLine>
    status: OrderStatus
  }

  class SalesOrderLine {
    lineNumber: Integer
    product: Product
    quantity: Quantity
    unitPrice: Money
    amount: SalesAmount
  }

  class Shipment {
    shipmentNumber: String
    salesOrder: SalesOrder
    shipDate: LocalDate
    status: ShipmentStatus
  }

  class Sales {
    salesNumber: String
    shipment: Shipment
    salesDate: LocalDate
    amount: Money
  }

  SalesOrder *-- SalesOrderLine
  Shipment --> SalesOrder
  Sales --> Shipment
}
@enduml
```

- **受注管理**: 受注登録、受注明細管理、受注ルールチェック
- **出荷管理**: 出荷指示、出荷実績登録
- **売上管理**: 売上計上、売上検索
- **請求管理**: 請求書作成、締め処理
- **回収管理**: 入金登録、消込処理

#### 調達（発注 → 仕入 → 支払）

調達プロセス全体を一元管理します。

```plantuml
@startuml
package "調達管理" {
  class PurchaseOrder {
    purchaseOrderNumber: String
    orderDate: LocalDate
    vendor: Vendor
    lines: List<PurchaseOrderLine>
    status: PurchaseOrderStatus
  }

  class PurchaseOrderLine {
    lineNumber: Integer
    product: Product
    quantity: Quantity
    unitPrice: Money
  }

  class Purchase {
    purchaseNumber: String
    purchaseOrder: PurchaseOrder
    purchaseDate: LocalDate
    amount: Money
  }

  class Payment {
    paymentNumber: String
    vendor: Vendor
    paymentDate: LocalDate
    amount: Money
  }

  PurchaseOrder *-- PurchaseOrderLine
  Purchase --> PurchaseOrder
  Payment --> Vendor
}
@enduml
```

- **発注管理**: 発注登録、発注明細管理、発注ステータス管理
- **仕入管理**: 仕入確認、仕入計上
- **支払管理**: 支払予定作成、支払実績登録

#### 在庫管理

在庫の入出庫と現在庫を管理します。

```plantuml
@startuml
package "在庫管理" {
  class Warehouse {
    warehouseId: String
    warehouseName: String
    warehouseCode: String
  }

  class Location {
    locationId: String
    locationName: String
    locationCode: String
    warehouse: Warehouse
  }

  class Inventory {
    inventoryId: String
    product: Product
    warehouse: Warehouse
    location: Location
    quantity: Quantity
    inventoryDate: LocalDate
  }

  Warehouse *-- Location
  Inventory --> Warehouse
  Inventory --> Location
  Inventory --> Product
}
@enduml
```

- **倉庫管理**: 倉庫マスタの登録・管理
- **棚番管理**: ロケーション（棚番）の登録・管理
- **在庫管理**: 在庫数量の登録・参照、在庫ルール管理

---

## 1.3 開発手法

### エクストリームプログラミング（XP）の採用

本プロジェクトでは、アジャイル開発手法の一つであるエクストリームプログラミング（XP）を採用しています。

```plantuml
@startmindmap
* XP
** 価値
*** コミュニケーション
*** シンプリシティ
*** フィードバック
*** 勇気
*** リスペクト
** 原則
*** 人間性
*** 経済性
*** ベイビーステップ
*** 改善
*** 品質
** プラクティス
*** テストファースト
*** ペアプログラミング
*** 継続的インテグレーション
*** リファクタリング
*** シンプルな設計
@endmindmap
```

#### XP の価値

| 価値 | 本プロジェクトでの実践 |
|------|----------------------|
| コミュニケーション | コードとテストによる意図の明示化 |
| シンプリシティ | 必要最小限の実装から始める |
| フィードバック | TDD による即座の検証 |
| 勇気 | 大胆なリファクタリング |
| リスペクト | コード品質への継続的な配慮 |

### テスト駆動開発（TDD）

本プロジェクトでは、テスト駆動開発（Test-Driven Development）を実践しています。

```plantuml
@startuml
title TDD サイクル

state "Red" as red
state "Green" as green
state "Refactor" as refactor

[*] --> red
red : 失敗するテストを書く
red --> green : テストが失敗することを確認
green : テストを通す最小限のコードを書く
green --> refactor : テストが成功することを確認
refactor : コードを改善する
refactor --> red : テストが成功したまま
refactor --> [*] : 機能完成

@enduml
```

#### TDD の3つのステップ

1. **Red（レッド）**: まず失敗するテストを書く
2. **Green（グリーン）**: テストを通す最小限のコードを実装する
3. **Refactor（リファクタ）**: テストを維持しながらコードを改善する

#### TDD の効果

- **設計の改善**: テストしやすいコードは疎結合で凝集度が高い
- **ドキュメント**: テストがコードの仕様書となる
- **リグレッション防止**: 変更による退行を即座に検出
- **開発リズム**: 小さなサイクルで着実に前進

### 継続的インテグレーション

変更を頻繁に統合し、自動テストで品質を担保します。

```plantuml
@startuml
title CI/CD パイプライン

rectangle "開発者" {
  (コード変更) --> (コミット)
}

rectangle "CI サーバー" {
  (コミット) --> (ビルド)
  (ビルド) --> (単体テスト)
  (単体テスト) --> (統合テスト)
  (統合テスト) --> (E2E テスト)
  (E2E テスト) --> (品質チェック)
}

rectangle "成果物" {
  (品質チェック) --> (デプロイ可能)
}

@enduml
```

- **10分ビルド**: ビルドと全テストを10分以内に完了
- **自動テスト**: 単体・統合・E2E テストの自動実行
- **品質チェック**: SonarQube によるコード品質分析

### イテレーティブな開発サイクル

週次・四半期のサイクルで計画と振り返りを繰り返します。

```plantuml
@startuml
title イテレーション開発

[*] --> 計画
計画 : ストーリーの選択
計画 : タスク分解
計画 --> 実装
実装 : TDD でコーディング
実装 : コードレビュー
実装 --> テスト
テスト : 受け入れテスト
テスト : E2E テスト
テスト --> 振り返り
振り返り : 成果の確認
振り返り : 改善点の特定
振り返り --> 計画 : 次のイテレーション
振り返り --> [*] : リリース

@enduml
```

---

## 1.4 本書の構成

### 各章で扱う機能とテーマ

本書は8部23章で構成され、販売管理システムの開発過程を段階的に解説します。

```plantuml
@startmindmap
* 販売管理システムのケーススタディ
** 第1部: 導入と基盤
*** 第1章: プロジェクト概要
*** 第2章: 開発環境の構築
*** 第3章: アーキテクチャ設計
** 第2部: データモデリング
*** 第4章: データモデル設計の基礎
*** 第5章: マスタデータモデル
*** 第6章: トランザクションデータモデル
*** 第7章: ドメインモデルとデータモデルの対応
*** 第8章: ドメインに適したデータの作成
** 第3部: マスタ管理機能
*** 第9章: 認証・ユーザー管理
*** 第10章: 部門・社員マスタ
*** 第11章: 商品マスタ
*** 第12章: 取引先管理
** 第4部: 販売管理機能
*** 第13章: 受注管理
*** 第14章: 出荷・売上管理
*** 第15章: 請求・回収管理
** 第5部: 調達管理機能
*** 第16章: 発注管理
*** 第17章: 仕入・支払管理
** 第6部: 在庫管理機能
*** 第18章: 在庫管理
** 第7部: 品質とリファクタリング
*** 第19章: テスト戦略
*** 第20章: 継続的リファクタリング
*** 第21章: アーキテクチャの検証
** 第8部: 運用と発展
*** 第22章: リリース管理
*** 第23章: 今後の展望
@endmindmap
```

#### 第1部: 導入と基盤（第1〜3章）

システムの概要と開発環境、アーキテクチャの基盤を解説します。

#### 第2部: データモデリング（第4〜8章）

広域データモデリングを先行して実施し、システム全体のデータ構造を設計します。これにより、後続の機能開発時にデータモデルの妥当性が保証されます。

#### 第3部〜第6部: 機能開発（第9〜18章）

各機能を TDD で実装していく過程を、実際のコードとともに解説します。

#### 第7部: 品質とリファクタリング（第19〜21章）

テスト戦略、継続的なリファクタリング、アーキテクチャの検証について解説します。

#### 第8部: 運用と発展（第22〜23章）

リリース管理と今後の展望について解説します。

### コードリポジトリの活用方法

本書のコードは GitHub で公開されています。

#### リポジトリ構成

```
case-study-sales/
├── app/
│   ├── backend/
│   │   └── api/          # Spring Boot バックエンド
│   └── frontend/
│       └── sms/          # React フロントエンド
├── docs/
│   ├── article/          # 本書原稿
│   ├── assets/           # 図表、仕様書
│   ├── reference/        # リファレンスガイド
│   └── wiki/             # 開発 Wiki
└── scripts/              # ビルド・開発スクリプト
```

#### 章ごとのブランチ/タグ

各章の完成時点のコードは、対応するバージョンタグで参照できます。

| 章 | バージョン | 内容 |
|----|-----------|------|
| 第9章完了 | v0.1.0 | 認証・ユーザー管理 |
| 第10章完了 | v0.2.0 | 部門・社員マスタ |
| 第11章完了 | v0.3.0 | 商品マスタ |
| 第12章完了 | v0.4.0 | 取引先管理 |
| 第13章完了 | v0.6.0 | 受注管理 |
| 第16章完了 | v0.7.0 | 発注管理 |
| 第14章完了 | v0.8.0 | 出荷・売上管理 |
| 第18章完了 | v0.10.0 | 在庫管理 |
| 第17章完了 | v0.11.0 | 仕入・支払管理 |

#### 実行方法

```bash
# リポジトリのクローン
git clone https://github.com/your-repo/case-study-sales.git
cd case-study-sales

# 特定バージョンへの切り替え
git checkout v0.1.0

# バックエンドの起動
cd app/backend/api
./gradlew bootRun

# フロントエンドの起動（別ターミナル）
cd app/frontend/sms
npm install
npm run dev
```

---

## まとめ

本章では、販売管理システムの概要と本書の構成について説明しました。

- 販売管理システムは**販売・調達・在庫**の3つの業務領域をカバーする
- **エクストリームプログラミング（XP）**と**テスト駆動開発（TDD）**を開発手法として採用
- 本書は**広域データモデリング**を先行し、**TDD による実装**を通じてプロジェクトを再現可能な構成

次章では、開発環境の構築について解説します。
