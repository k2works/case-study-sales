# 第4章: データモデル設計の基礎

## 4.1 ER モデリングの原則

### エンティティの識別

エンティティとは、システムで管理すべき「もの」や「こと」を表す概念です。販売管理システムでは、以下のようなエンティティを識別します。

```plantuml
@startuml
title エンティティの分類

package "マスタエンティティ" {
  entity "部門" as dept
  entity "社員" as emp
  entity "商品" as prod
  entity "取引先" as partner
  entity "倉庫" as wh
}

package "トランザクションエンティティ" {
  entity "受注" as order
  entity "発注" as po
  entity "在庫" as stock
  entity "売上" as sales
  entity "仕入" as purchase
}

package "リソースエンティティ" {
  entity "ユーザー" as user
  entity "実行履歴" as audit
}

@enduml
```

#### エンティティの種類

| 種類 | 説明 | 例 |
|------|------|-----|
| マスタエンティティ | 比較的変更が少ない基本情報 | 部門、社員、商品、取引先 |
| トランザクションエンティティ | 業務活動の記録 | 受注、発注、在庫、売上 |
| リソースエンティティ | システム運用に必要な情報 | ユーザー、実行履歴 |

#### エンティティ識別のポイント

1. **独立して存在できるか**: 他のエンティティに依存せず、単独で意味を持つか
2. **一意に識別できるか**: 主キーによって各インスタンスを区別できるか
3. **複数のインスタンスを持つか**: 一覧として管理する必要があるか

### リレーションシップの設計

エンティティ間の関係を定義します。

```plantuml
@startuml
title リレーションシップの種類

entity "部門" as dept {
  * 部門コード [PK]
  --
  部門名
}

entity "社員" as emp {
  * 社員コード [PK]
  --
  社員名
  部門コード [FK]
}

entity "受注" as order {
  * 受注番号 [PK]
  --
  受注日
  顧客コード [FK]
}

entity "受注明細" as order_line {
  * 受注番号 [PK][FK]
  * 受注行番号 [PK]
  --
  商品コード [FK]
  数量
}

dept ||--o{ emp : "所属"
order ||--|{ order_line : "含む"

@enduml
```

#### カーディナリティ（多重度）

| 記法 | 意味 | 例 |
|------|------|-----|
| `1:1` | 1対1 | ユーザー - 認証情報 |
| `1:N` | 1対多 | 部門 - 社員 |
| `N:M` | 多対多 | 商品 - 取引先（顧客別単価） |

#### 依存関係の種類

| 種類 | 説明 | 例 |
|------|------|-----|
| 識別依存 | 親の主キーが子の主キーの一部 | 受注 - 受注明細 |
| 非識別依存 | 親の主キーが子の外部キー | 部門 - 社員 |

### 正規化と非正規化のトレードオフ

データの冗長性を排除しつつ、パフォーマンスを考慮した設計が必要です。

```plantuml
@startuml
title 正規化のレベル

rectangle "非正規形" as unnorm {
  note "重複データを含む\n更新異常が発生しやすい" as n1
}

rectangle "第1正規形" as 1nf {
  note "繰り返し項目を排除\n各列は原子値" as n2
}

rectangle "第2正規形" as 2nf {
  note "部分関数従属を排除\n主キー全体に依存" as n3
}

rectangle "第3正規形" as 3nf {
  note "推移的関数従属を排除\n非キー列同士の依存なし" as n4
}

unnorm --> 1nf
1nf --> 2nf
2nf --> 3nf

@enduml
```

#### 本システムでの判断

| 観点 | 正規化 | 非正規化 |
|------|--------|----------|
| データ整合性 | 高い | 低い |
| 更新性能 | 高い | 低い |
| 参照性能 | 低い（結合が必要） | 高い |
| ストレージ | 少ない | 多い |

本システムでは、基本的に第3正規形を採用しつつ、以下の場合に非正規化を検討します。

- **商品名の複製**: 受注明細に商品名を保持（履歴保持のため）
- **金額合計の保持**: 受注ヘッダに金額合計を保持（集計性能のため）

---

## 4.2 販売管理システムの全体データモデル

### 主要エンティティの洗い出し

本システムの主要エンティティを以下に示します。

```plantuml
@startuml
title 販売管理システム 全体データモデル（概要）

' マスタ系
package "マスタ" {
  entity "部門マスタ" as dept
  entity "社員マスタ" as emp
  entity "商品マスタ" as prod
  entity "商品分類マスタ" as prod_cat
  entity "取引先マスタ" as partner
  entity "取引先グループマスタ" as partner_grp
  entity "顧客マスタ" as customer
  entity "仕入先マスタ" as vendor
  entity "倉庫マスタ" as wh
  entity "棚番マスタ" as loc
}

' 販売系
package "販売" {
  entity "受注データ" as order
  entity "受注データ明細" as order_line
  entity "売上データ" as sales
  entity "売上データ明細" as sales_line
}

' 調達系
package "調達" {
  entity "発注データ" as po
  entity "発注データ明細" as po_line
  entity "仕入データ" as purchase
  entity "仕入データ明細" as purchase_line
}

' 在庫系
package "在庫" {
  entity "在庫データ" as stock
}

' リレーション（主要なもののみ）
partner_grp ||--o{ partner
partner ||--o| customer
partner ||--o| vendor
prod_cat ||--o{ prod
dept ||--o{ emp
wh ||--o{ loc

customer ||--o{ order
order ||--|{ order_line
prod }o--|| order_line

vendor ||--o{ po
po ||--|{ po_line
prod }o--|| po_line

wh ||--o{ stock
prod }o--|| stock

@enduml
```

### ドメイン境界の設定

システムを以下のドメイン境界で分割しています。

```plantuml
@startuml
title ドメイン境界

rectangle "システム管理" as system {
  (ユーザー)
  (実行履歴)
}

rectangle "マスタ管理" as master {
  (部門)
  (社員)
  (商品)
  (取引先)
}

rectangle "販売管理" as sales {
  (受注)
  (出荷)
  (売上)
  (請求)
}

rectangle "調達管理" as purchase {
  (発注)
  (仕入)
  (支払)
}

rectangle "在庫管理" as stock {
  (倉庫)
  (在庫)
}

master --> sales : 参照
master --> purchase : 参照
master --> stock : 参照
sales --> stock : 在庫引当
purchase --> stock : 入庫

@enduml
```

### データフローの可視化

販売・調達プロセスにおけるデータの流れを示します。

```plantuml
@startuml
title 販売プロセスのデータフロー

|顧客|
start
:注文;

|販売管理|
:受注登録;
:受注データ作成;

|在庫管理|
:在庫引当;
:在庫データ更新;

|販売管理|
:出荷指示;
:売上計上;
:売上データ作成;

|顧客|
:商品受領;

stop

@enduml
```

```plantuml
@startuml
title 調達プロセスのデータフロー

|調達管理|
start
:発注登録;
:発注データ作成;

|仕入先|
:注文受付;
:出荷;

|調達管理|
:入荷確認;
:仕入データ作成;

|在庫管理|
:入庫処理;
:在庫データ更新;

|調達管理|
:支払処理;

stop

@enduml
```

---

## 4.3 JIG-ERD によるモデル可視化

### ER 図の自動生成

JIG-ERD は MyBatis マッパーから ER 図を自動生成するツールです。

```java
// テストコードで ER 図を生成
@Test
void generateErDiagram() {
    var output = Path.of("build/jig-erd");
    var packageName = "com.example.sms.infrastructure.datasource";

    JigErd.run(output, packageName);
}
```

#### 生成コマンド

```bash
./gradlew test --tests "*JigErdTest*"
```

### 概要・サマリー・詳細の使い分け

JIG-ERD は3つのレベルの ER 図を生成します。

```plantuml
@startuml
title JIG-ERD の出力レベル

rectangle "概要（Overview）" as overview {
  note "テーブル名のみ表示\n全体構造の把握に有効" as n1
}

rectangle "サマリー（Summary）" as summary {
  note "主キー・外部キーを表示\n主要なリレーションを確認" as n2
}

rectangle "詳細（Detail）" as detail {
  note "全カラムを表示\nテーブル設計の詳細確認" as n3
}

overview --> summary : より詳細に
summary --> detail : より詳細に

@enduml
```

#### 各レベルの用途

| レベル | ファイル名 | 用途 |
|--------|-----------|------|
| 概要 | library-er-overview.svg | 全体構造の把握、新規メンバーへの説明 |
| サマリー | library-er-summary.svg | リレーションの確認、設計レビュー |
| 詳細 | library-er-detail.svg | 実装時の参照、テーブル定義の確認 |

### 設計レビューへの活用

JIG-ERD で生成した ER 図は、以下の場面で活用します。

#### リリースごとのアーカイブ

```
docs/assets/release/
├── v0_1_0/
│   └── jig-erd/
│       ├── library-er-overview.svg
│       ├── library-er-summary.svg
│       └── library-er-detail.svg
├── v0_2_0/
│   └── jig-erd/
│       └── ...
└── v0_11_0/
    └── jig-erd/
        └── ...
```

#### 変更の追跡

バージョン間の ER 図を比較することで、データモデルの変更を視覚的に追跡できます。

```plantuml
@startuml
title データモデルの変更追跡

rectangle "v0.1.0" as v1 {
  (ユーザー)
  (部門)
  (社員)
}

rectangle "v0.4.0" as v4 {
  (ユーザー)
  (部門)
  (社員)
  (取引先) #lightgreen
  (顧客) #lightgreen
  (仕入先) #lightgreen
}

rectangle "v0.6.0" as v6 {
  (ユーザー)
  (部門)
  (社員)
  (取引先)
  (顧客)
  (仕入先)
  (受注) #lightgreen
  (受注明細) #lightgreen
}

v1 --> v4 : 取引先追加
v4 --> v6 : 受注追加

@enduml
```

---

## 主要テーブル一覧

本システムの主要テーブルを以下に示します。

### マスタ系テーブル

| テーブル名 | 説明 | 主キー |
|-----------|------|--------|
| 部門マスタ | 組織の部門情報 | 部門コード, 開始日 |
| 社員マスタ | 社員の基本情報 | 社員コード |
| 商品マスタ | 商品の基本情報 | 商品コード |
| 商品分類マスタ | 商品の分類階層 | 商品分類コード |
| 取引先マスタ | 取引先の基本情報 | 取引先コード |
| 取引先グループマスタ | 取引先のグループ | 取引先グループコード |
| 顧客マスタ | 顧客固有の情報 | 顧客コード, 顧客枝番 |
| 仕入先マスタ | 仕入先固有の情報 | 仕入先コード, 仕入先枝番 |
| 倉庫マスタ | 倉庫の基本情報 | 倉庫コード |
| 棚番マスタ | 倉庫内のロケーション | 倉庫コード, 棚番コード, 商品コード |

### トランザクション系テーブル

| テーブル名 | 説明 | 主キー |
|-----------|------|--------|
| 受注データ | 受注ヘッダ | 受注番号 |
| 受注データ明細 | 受注明細行 | 受注番号, 受注行番号 |
| 発注データ | 発注ヘッダ | 発注番号 |
| 発注データ明細 | 発注明細行 | 発注番号, 発注行番号 |
| 在庫データ | 在庫残高 | 倉庫コード, 商品コード, ロット番号, 在庫区分, 良品区分 |

---

## まとめ

本章では、データモデル設計の基礎について解説しました。

- **エンティティの識別**: マスタ、トランザクション、リソースの3種類に分類
- **リレーションシップ**: カーディナリティと依存関係の設計
- **正規化**: 第3正規形を基本とし、必要に応じて非正規化
- **全体モデル**: マスタ、販売、調達、在庫のドメイン境界
- **JIG-ERD**: 概要・サマリー・詳細の3レベルで可視化

次章では、マスタデータモデルの詳細について解説します。
