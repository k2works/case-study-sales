# 第6章: トランザクションデータモデル

## 6.1 販売トランザクション

### 受注ヘッダと受注明細

販売業務の中心となる受注データは、ヘッダ・明細パターンで設計されています。

```plantuml
@startuml
title 受注データの構造

entity "受注データ" as order {
  * 受注番号 [PK]
  --
  受注日
  部門コード [FK]
  部門開始日 [FK]
  顧客コード [FK]
  顧客枝番 [FK]
  社員コード [FK]
  希望納期
  客先注文番号
  倉庫コード [FK]
  受注金額合計
  消費税合計
  備考
  ...
}

entity "受注データ明細" as order_line {
  * 受注番号 [PK][FK]
  * 受注行番号 [PK]
  --
  商品コード [FK]
  商品名
  販売単価
  受注数量
  消費税率
  引当数量
  出荷指示数量
  出荷済数量
  完了フラグ
  値引金額
  納期
  出荷日
  ...
}

order ||--|{ order_line : "明細"

note right of order
  受注全体の情報
  顧客、担当者、納期など
end note

note right of order_line
  商品ごとの情報
  数量、単価、進捗状況など
end note

@enduml
```

#### 受注データのテーブル定義

```sql
create table if not exists 受注データ
(
    受注番号     varchar(10)                       not null
        constraint pk_orders primary key,
    受注日       timestamp(6) default CURRENT_DATE not null,
    部門コード   varchar(6)                        not null,
    部門開始日   timestamp(6) default CURRENT_DATE not null,
    顧客コード   varchar(8)                        not null,
    顧客枝番     integer,
    社員コード   varchar(10)                       not null,
    希望納期     timestamp(6),
    客先注文番号 varchar(20),
    倉庫コード   varchar(3)                        not null,
    受注金額合計 integer      default 0            not null,
    消費税合計   integer      default 0            not null,
    備考         varchar(1000),
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    version      integer      default 0
);
```

#### 受注データ明細のテーブル定義

```sql
create table if not exists 受注データ明細
(
    受注番号     varchar(10)                       not null
        references 受注データ
        on update cascade on delete restrict,
    受注行番号   integer                           not null,
    商品コード   varchar(16)                       not null,
    商品名       varchar(10)                       not null,
    販売単価     integer      default 0            not null,
    受注数量     integer      default 1            not null,
    消費税率     integer      default 0,
    引当数量     integer      default 0,
    出荷指示数量 integer      default 0,
    出荷済数量   integer      default 0,
    完了フラグ   integer      default 0            not null,
    値引金額     integer      default 0            not null,
    納期         timestamp(6),
    出荷日       timestamp(6),
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    primary key (受注番号, 受注行番号)
);
```

#### ヘッダ・明細パターンの特徴

| 観点 | ヘッダ（受注データ） | 明細（受注データ明細） |
|------|----------------------|------------------------|
| 識別 | 受注番号で一意 | 受注番号 + 行番号で一意 |
| 内容 | 取引全体の情報 | 商品ごとの詳細 |
| カーディナリティ | 1 | N（複数行） |
| 関連マスタ | 顧客、部門、社員 | 商品 |

### 出荷・売上データ

本システムでは、受注明細の進捗状況を複数の数量項目で管理しています。

```plantuml
@startuml
title 受注から出荷までの数量管理

rectangle "受注数量" as order_qty {
  note "顧客から注文を受けた数量" as n1
}

rectangle "引当数量" as alloc_qty {
  note "在庫を確保した数量" as n2
}

rectangle "出荷指示数量" as ship_instr_qty {
  note "出荷を指示した数量" as n3
}

rectangle "出荷済数量" as shipped_qty {
  note "実際に出荷した数量" as n4
}

order_qty --> alloc_qty : "在庫引当"
alloc_qty --> ship_instr_qty : "出荷指示"
ship_instr_qty --> shipped_qty : "出荷完了"

@enduml
```

#### 数量項目の関係

| 項目 | 説明 | 更新タイミング |
|------|------|----------------|
| 受注数量 | 注文を受けた数量 | 受注登録時 |
| 引当数量 | 在庫を確保した数量 | 在庫引当処理時 |
| 出荷指示数量 | 出荷を指示した数量 | 出荷指示時 |
| 出荷済数量 | 出荷完了した数量 | 出荷完了時 |

#### 完了フラグの判定

```plantuml
@startuml
title 明細の完了判定

start
:受注明細を取得;

if (受注数量 == 出荷済数量?) then (はい)
  :完了フラグ = 1;
else (いいえ)
  :完了フラグ = 0;
endif

stop

note right
  すべての数量が出荷されたら完了
  部分出荷の場合は未完了のまま
end note

@enduml
```

### 請求・入金データ

本システムでは、請求・入金データは将来の拡張として設計されています。現時点では受注・出荷・売上までをスコープとしています。

```plantuml
@startuml
title 販売プロセスの全体像

rectangle "受注" as order #lightblue
rectangle "在庫引当" as alloc #lightblue
rectangle "出荷" as ship #lightblue
rectangle "売上" as sales #lightblue
rectangle "請求" as invoice #lightgray
rectangle "入金" as payment #lightgray

order --> alloc
alloc --> ship
ship --> sales
sales --> invoice : "将来実装"
invoice --> payment : "将来実装"

note bottom of invoice
  現時点ではスコープ外
end note

@enduml
```

---

## 6.2 調達トランザクション

### 発注ヘッダと発注明細

調達業務の発注データも、ヘッダ・明細パターンで設計されています。

```plantuml
@startuml
title 発注データの構造

entity "発注データ" as po {
  * 発注番号 [PK]
  --
  発注日
  受注番号
  仕入先コード [FK]
  仕入先枝番 [FK]
  発注担当者コード [FK]
  指定納期
  倉庫コード [FK]
  発注金額合計
  消費税合計
  備考
  ...
}

entity "発注データ明細" as po_line {
  * 発注番号 [PK][FK]
  * 発注行番号 [PK]
  --
  発注行表示番号
  受注番号
  受注行番号
  商品コード [FK]
  商品名
  発注単価
  発注数量
  入荷数量
  完了フラグ
  ...
}

po ||--|{ po_line : "明細"

note right of po::受注番号
  受注に紐づく発注
  受注起点の場合に設定
end note

note right of po_line::受注番号
  受注明細との紐付け
  トレーサビリティ確保
end note

@enduml
```

#### 発注データのテーブル定義

```sql
create table if not exists 発注データ
(
    発注番号         varchar(10)                       not null
        constraint pk_purchase_orders primary key,
    発注日           timestamp(6),
    受注番号         varchar(10)                       not null,
    仕入先コード     varchar(8)                        not null,
    仕入先枝番       integer      default 0,
    発注担当者コード varchar(10)                       not null,
    指定納期         timestamp(6),
    倉庫コード       varchar(3)                        not null,
    発注金額合計     integer      default 0,
    消費税合計       integer      default 0            not null,
    備考             varchar(1000),
    作成日時         timestamp(6) default CURRENT_DATE not null,
    作成者名         varchar(12),
    更新日時         timestamp(6) default CURRENT_DATE not null,
    更新者名         varchar(12),
    version          integer      default 1            not null
);
```

#### 発注データ明細のテーブル定義

```sql
create table if not exists 発注データ明細
(
    発注番号       varchar(10)                       not null
        references 発注データ
        on update cascade on delete restrict,
    発注行番号     integer                           not null,
    発注行表示番号 integer                           not null,
    受注番号       varchar(10)                       not null,
    受注行番号     integer                           not null,
    商品コード     varchar(16)                       not null,
    商品名         varchar(10)                       not null,
    発注単価       integer      default 0,
    発注数量       integer      default 1            not null,
    入荷数量       integer      default 1            not null,
    完了フラグ     integer      default 0            not null,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12),
    version        integer      default 1            not null,
    constraint pk_purchase_order_details
        primary key (発注行番号, 発注番号)
);
```

### 仕入・支払データ

仕入・支払データも将来の拡張として設計されています。

```plantuml
@startuml
title 調達プロセスの全体像

rectangle "発注" as po #lightblue
rectangle "入荷" as receive #lightblue
rectangle "仕入" as purchase #lightgray
rectangle "支払" as payment #lightgray

po --> receive
receive --> purchase : "将来実装"
purchase --> payment : "将来実装"

note bottom of purchase
  現時点ではスコープ外
end note

@enduml
```

### 発注から支払までのデータフロー

受注と発注の連携を含めた全体のデータフローを示します。

```plantuml
@startuml
title 受発注のデータフロー

|販売|
start
:受注登録;
:受注データ作成;
:受注データ明細作成;

|調達|
:発注登録;
note right
  受注番号を保持
  トレーサビリティ確保
end note
:発注データ作成;
:発注データ明細作成;

|仕入先|
:出荷;

|調達|
:入荷確認;
:入荷数量を更新;

|在庫|
:入庫処理;
:在庫データ更新;

|販売|
:在庫引当;
:引当数量を更新;
:出荷;
:出荷済数量を更新;

stop

@enduml
```

### 受注と発注の紐付け

受注から発注への紐付けにより、トレーサビリティを確保しています。

```plantuml
@startuml
title 受注と発注の関連

entity "受注データ" as order {
  * 受注番号 [PK]
  --
  ...
}

entity "受注データ明細" as order_line {
  * 受注番号 [PK][FK]
  * 受注行番号 [PK]
  --
  ...
}

entity "発注データ" as po {
  * 発注番号 [PK]
  --
  受注番号
  ...
}

entity "発注データ明細" as po_line {
  * 発注番号 [PK][FK]
  * 発注行番号 [PK]
  --
  受注番号
  受注行番号
  ...
}

order ||--|{ order_line
po ||--|{ po_line

order ||--o{ po : "起因"
order_line ||--o{ po_line : "対応"

note bottom of po
  1つの受注に対して
  複数の発注が可能
end note

@enduml
```

---

## 6.3 在庫トランザクション

### 在庫移動の記録

在庫の増減は、受注・発注に連動して記録されます。

```plantuml
@startuml
title 在庫の増減パターン

rectangle "入庫" as in {
  (仕入入庫)
  (返品入庫)
  (振替入庫)
}

rectangle "出庫" as out {
  (売上出庫)
  (返品出庫)
  (振替出庫)
}

rectangle "在庫データ" as stock

in --> stock : "増加"
stock --> out : "減少"

@enduml
```

#### 在庫データの更新タイミング

| イベント | 実在庫数 | 有効在庫数 | 備考 |
|----------|----------|------------|------|
| 仕入入庫 | +n | +n | 入荷検収完了時 |
| 在庫引当 | - | -n | 受注確定時 |
| 出荷 | -n | - | 出荷完了時 |
| 引当解除 | - | +n | 受注取消時 |

### 在庫残高の計算方法

在庫残高は、トランザクションの積み重ねではなく、現在値として管理しています。

```plantuml
@startuml
title 在庫残高の管理方式

rectangle "残高方式（本システム）" as balance {
  note "現在の残高を直接保持\n更新時に直接加減算" as n1
}

rectangle "移動方式（参考）" as movement {
  note "入出庫履歴を保持\n残高は都度計算" as n2
}

@enduml
```

#### 残高方式のメリット・デメリット

| 観点 | メリット | デメリット |
|------|----------|------------|
| 参照性能 | 残高を直接取得可能 | - |
| 履歴管理 | - | 履歴は別途管理が必要 |
| 整合性 | - | 排他制御が重要 |
| 実装 | シンプル | 移動履歴との乖離リスク |

### 楽観的ロックによる排他制御

在庫データの更新には、version 列による楽観的ロックを使用しています。

```plantuml
@startuml
title 楽観的ロックの流れ

participant "プロセスA" as A
participant "在庫データ" as Stock
participant "プロセスB" as B

A -> Stock: SELECT (version=1)
B -> Stock: SELECT (version=1)
A -> Stock: UPDATE SET version=2 WHERE version=1
A <- Stock: 成功
B -> Stock: UPDATE SET version=2 WHERE version=1
B <- Stock: 失敗（0件更新）
B -> B: リトライまたはエラー

@enduml
```

```sql
-- 楽観的ロックの更新例
UPDATE 在庫データ
SET 実在庫数 = 実在庫数 - 10,
    有効在庫数 = 有効在庫数 - 10,
    version = version + 1
WHERE 倉庫コード = '001'
  AND 商品コード = 'P001'
  AND ロット番号 = 'LOT001'
  AND 在庫区分 = '1'
  AND 良品区分 = 'G'
  AND version = 1;
```

---

## 6.4 ステータス管理

### 状態遷移の設計

受注・発注の状態は、数量項目の値から判定します。

```plantuml
@startuml
title 受注明細の状態遷移

[*] --> 未引当 : 受注登録

未引当 --> 引当済 : 在庫引当
引当済 --> 未引当 : 引当解除

引当済 --> 出荷指示済 : 出荷指示
出荷指示済 --> 引当済 : 指示取消

出荷指示済 --> 出荷済 : 出荷完了
出荷済 --> [*] : 完了

state 未引当 : 引当数量 = 0
state 引当済 : 引当数量 > 0, 出荷指示数量 = 0
state 出荷指示済 : 出荷指示数量 > 0, 出荷済数量 = 0
state 出荷済 : 出荷済数量 > 0

@enduml
```

#### 状態の判定ロジック

本システムでは、専用のステータス列を持たず、数量項目から状態を導出します。

```java
// 状態判定の例（擬似コード）
public OrderLineStatus getStatus(OrderLine line) {
    if (line.getShippedQuantity() >= line.getOrderQuantity()) {
        return OrderLineStatus.COMPLETED;
    }
    if (line.getShipInstructionQuantity() > 0) {
        return OrderLineStatus.SHIP_INSTRUCTED;
    }
    if (line.getAllocatedQuantity() > 0) {
        return OrderLineStatus.ALLOCATED;
    }
    return OrderLineStatus.NOT_ALLOCATED;
}
```

#### 状態管理のアプローチ比較

| アプローチ | 説明 | 本システムの採用 |
|------------|------|------------------|
| ステータス列 | 状態を専用列で管理 | 部分採用（完了フラグ） |
| 数量導出 | 数量から状態を計算 | 採用 |
| イベントソーシング | イベント履歴から状態を再構築 | 不採用 |

### 履歴テーブルの活用

トランザクションデータの変更履歴は、監査証跡として重要です。

```plantuml
@startuml
title 履歴管理の方針

rectangle "トランザクションデータ" as trans {
  note "現在の状態を保持\n作成日時・更新日時を記録" as n1
}

rectangle "監査ログ（別途）" as audit {
  note "変更操作を記録\n誰が・いつ・何を" as n2
}

trans --> audit : "変更時に記録"

@enduml
```

#### 監査項目

| 項目 | 説明 |
|------|------|
| 作成日時 | レコード作成日時 |
| 作成者名 | レコード作成者 |
| 更新日時 | 最終更新日時 |
| 更新者名 | 最終更新者 |
| version | 楽観的ロック用バージョン |

---

## トランザクション間の関連

販売・調達・在庫のトランザクションは相互に関連しています。

```plantuml
@startuml
title トランザクション間の関連

entity "受注データ" as order {
  * 受注番号 [PK]
  --
  顧客コード
  倉庫コード
  ...
}

entity "受注データ明細" as order_line {
  * 受注番号 [PK][FK]
  * 受注行番号 [PK]
  --
  商品コード
  引当数量
  ...
}

entity "発注データ" as po {
  * 発注番号 [PK]
  --
  受注番号
  仕入先コード
  倉庫コード
  ...
}

entity "発注データ明細" as po_line {
  * 発注番号 [PK][FK]
  * 発注行番号 [PK]
  --
  受注番号
  受注行番号
  商品コード
  ...
}

entity "在庫データ" as stock {
  * 倉庫コード [PK]
  * 商品コード [PK]
  * ロット番号 [PK]
  * 在庫区分 [PK]
  * 良品区分 [PK]
  --
  実在庫数
  有効在庫数
  ...
}

order ||--|{ order_line
po ||--|{ po_line
order ||--o{ po : "起因"
order_line ||--o{ po_line : "対応"
order_line }o--|| stock : "引当"
po_line }o--|| stock : "入庫先"

@enduml
```

---

## まとめ

本章では、トランザクションデータモデルの詳細について解説しました。

- **販売トランザクション**: ヘッダ・明細パターンによる受注管理、数量による進捗管理
- **調達トランザクション**: 受注との紐付けによるトレーサビリティ確保
- **在庫トランザクション**: 残高方式による在庫管理、楽観的ロックによる排他制御
- **ステータス管理**: 数量からの状態導出、監査項目の標準化

次章では、ドメインモデルとデータモデルの対応について解説します。
