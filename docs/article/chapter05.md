# 第5章: マスタデータモデル

## 5.1 組織マスタ

### 部門の階層構造設計

組織を表現するデータモデルでは、部門の階層構造を適切に設計することが重要です。本システムでは、経路列挙モデル（Path Enumeration）を採用しています。

```plantuml
@startuml
title 部門マスタの構造

entity "部門マスタ" as dept {
  * 部門コード [PK]
  * 開始日 [PK]
  --
  終了日
  部門名
  組織階層
  部門パス
  最下層区分
  伝票入力可否
  作成日時
  作成者名
  更新日時
  更新者名
}

note right of dept::部門パス
  経路列挙モデル
  例: "10000~10100~10110"
  階層構造を文字列で表現
end note

note right of dept::組織階層
  0: 最上位
  1: 第1階層
  2: 第2階層
  ...
end note

@enduml
```

#### テーブル定義

```sql
create table if not exists 部門マスタ
(
    部門コード   varchar(6)                        not null,
    開始日       timestamp(6) default CURRENT_DATE not null,
    終了日       timestamp(6) default '2100-12-31 00:00:00',
    部門名       varchar(40),
    組織階層     integer      default 0            not null,
    部門パス     varchar(100)                      not null,
    最下層区分   integer      default 0            not null,
    伝票入力可否 integer      default 1            not null,
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    constraint pk_department
        primary key (部門コード, 開始日)
);
```

#### 階層構造の表現方法

```plantuml
@startuml
title 部門階層の例

rectangle "本社\n(10000)" as hq {
  rectangle "営業本部\n(10100)" as sales_hq {
    rectangle "東日本営業部\n(10110)" as east
    rectangle "西日本営業部\n(10120)" as west
  }
  rectangle "管理本部\n(10200)" as admin_hq {
    rectangle "経理部\n(10210)" as accounting
    rectangle "人事部\n(10220)" as hr
  }
}

@enduml
```

| 部門コード | 部門名 | 組織階層 | 部門パス |
|------------|--------|----------|----------|
| 10000 | 本社 | 0 | 10000 |
| 10100 | 営業本部 | 1 | 10000~10100 |
| 10110 | 東日本営業部 | 2 | 10000~10100~10110 |
| 10120 | 西日本営業部 | 2 | 10000~10100~10120 |
| 10200 | 管理本部 | 1 | 10000~10200 |
| 10210 | 経理部 | 2 | 10000~10200~10210 |
| 10220 | 人事部 | 2 | 10000~10200~10220 |

#### 経路列挙モデルの特徴

| 観点 | メリット | デメリット |
|------|----------|------------|
| 検索性能 | 前方一致検索で子孫を高速取得 | - |
| 階層取得 | パスから直接階層を判定可能 | - |
| 移動操作 | - | 部門移動時にパス更新が必要 |
| 整合性 | - | パスの整合性を別途管理 |

### 社員と部門の関連

社員マスタは部門マスタと関連を持ち、所属部門を表現します。

```plantuml
@startuml
title 社員と部門の関連

entity "部門マスタ" as dept {
  * 部門コード [PK]
  * 開始日 [PK]
  --
  部門名
  ...
}

entity "社員マスタ" as emp {
  * 社員コード [PK]
  --
  社員名
  社員名カナ
  パスワード
  電話番号
  FAX番号
  部門コード [FK]
  開始日 [FK]
  職種コード
  承認権限コード
  ...
}

dept ||--o{ emp : "所属"

@enduml
```

#### テーブル定義

```sql
create table if not exists 社員マスタ
(
    社員コード     varchar(10)                       not null
        constraint pk_employee primary key,
    社員名         varchar(20),
    社員名カナ     varchar(40),
    パスワード     varchar(8),
    電話番号       varchar(13),
    "FAX番号"      varchar(13),
    部門コード     varchar(6)                        not null,
    開始日         timestamp(6) default CURRENT_DATE not null,
    職種コード     varchar(2)                        not null,
    承認権限コード varchar(2)                        not null,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12)
);
```

### 再帰的なデータ構造の表現

部門の階層構造を表現する方法には、主に以下の4つがあります。

```plantuml
@startuml
title 階層構造の表現方法

rectangle "隣接リストモデル" as adj {
  note "親IDを持つ\n最もシンプル" as n1
}

rectangle "経路列挙モデル" as path {
  note "パスを文字列で保持\n本システムで採用" as n2
}

rectangle "入れ子集合モデル" as nested {
  note "左右の境界値を保持\n高度な検索が可能" as n3
}

rectangle "閉包テーブルモデル" as closure {
  note "全ての祖先-子孫関係を保持\n柔軟だがデータ量大" as n4
}

adj --> path : より高速な検索
path --> nested : より複雑な検索
nested --> closure : 最大の柔軟性

@enduml
```

#### 各モデルの比較

| モデル | 子孫取得 | 祖先取得 | 移動操作 | 実装複雑度 |
|--------|----------|----------|----------|------------|
| 隣接リスト | 再帰クエリ | 再帰クエリ | 簡単 | 低 |
| 経路列挙 | LIKE検索 | パス解析 | 中程度 | 中 |
| 入れ子集合 | 範囲検索 | 範囲検索 | 複雑 | 高 |
| 閉包テーブル | 単純結合 | 単純結合 | 中程度 | 中 |

本システムでは、検索性能と実装の複雑さのバランスから経路列挙モデルを採用しています。

---

## 5.2 商品マスタ

### 商品分類と商品の関連

商品は商品分類に属し、分類自体も階層構造を持ちます。

```plantuml
@startuml
title 商品分類と商品の関連

entity "商品分類マスタ" as cat {
  * 商品分類コード [PK]
  --
  商品分類名
  商品分類階層
  商品分類パス
  最下層区分
  ...
}

entity "商品マスタ" as prod {
  * 商品コード [PK]
  --
  商品正式名
  商品略称
  商品名カナ
  商品区分
  製品型番
  販売単価
  仕入単価
  売上原価
  税区分
  商品分類コード [FK]
  雑区分
  在庫管理対象区分
  在庫引当区分
  仕入先コード [FK]
  仕入先枝番
  ...
}

cat ||--o{ prod : "分類"

note right of prod::商品区分
  1: 商品
  2: 製品
  3: 原材料
  4: 間接材
end note

note right of prod::在庫引当区分
  0: 対象外
  1: 即時
  2: まとめ
  3: 手配品
end note

@enduml
```

#### 商品分類マスタのテーブル定義

```sql
create table if not exists 商品分類マスタ
(
    商品分類コード varchar(8)                        not null
        constraint pk_product_category primary key,
    商品分類名     varchar(30),
    商品分類階層   integer      default 0            not null,
    商品分類パス   varchar(100),
    最下層区分     integer      default 0,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12)
);
```

#### 商品マスタのテーブル定義

```sql
create table if not exists 商品マスタ
(
    商品コード       varchar(16)                       not null
        constraint pk_products primary key,
    商品正式名       varchar(40)                       not null,
    商品略称         varchar(10)                       not null,
    商品名カナ       varchar(20)                       not null,
    商品区分         varchar(1),
    製品型番         varchar(40),
    販売単価         integer      default 0            not null,
    仕入単価         integer      default 0,
    売上原価         integer      default 0            not null,
    税区分           integer      default 1            not null,
    商品分類コード   varchar(8),
    雑区分           integer,
    在庫管理対象区分 integer      default 1,
    在庫引当区分     integer,
    仕入先コード     varchar(8)                        not null,
    仕入先枝番       integer,
    作成日時         timestamp(6) default CURRENT_DATE not null,
    作成者名         varchar(12),
    更新日時         timestamp(6) default CURRENT_DATE not null,
    更新者名         varchar(12)
);
```

### 顧客別販売単価テーブル

特定の顧客に対して個別の販売単価を設定するためのテーブルです。

```plantuml
@startuml
title 顧客別販売単価の構造

entity "商品マスタ" as prod {
  * 商品コード [PK]
  --
  販売単価（標準）
  ...
}

entity "顧客別販売単価" as price {
  * 商品コード [PK][FK]
  * 取引先コード [PK]
  --
  販売単価
  ...
}

entity "取引先マスタ" as partner {
  * 取引先コード [PK]
  --
  取引先名
  ...
}

prod ||--o{ price : "個別単価"
partner ||--o{ price : "顧客"

@enduml
```

#### テーブル定義

```sql
create table if not exists 顧客別販売単価
(
    商品コード   varchar(16)                       not null
        references 商品マスタ
        on update cascade on delete restrict,
    取引先コード varchar(8)                        not null,
    販売単価     integer      default 0            not null,
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    constraint pk_pricebycustomer
        primary key (商品コード, 取引先コード)
);
```

#### 価格決定ロジック

```plantuml
@startuml
title 販売単価の決定フロー

start
:受注入力;
:顧客コードと商品コードを取得;

if (顧客別販売単価が存在？) then (はい)
  :顧客別販売単価を適用;
else (いいえ)
  :商品マスタの標準販売単価を適用;
endif

:単価を受注明細にセット;
stop

@enduml
```

### 価格履歴の管理

本システムでは、価格の履歴管理について以下の方針を採用しています。

```plantuml
@startuml
title 価格管理の方針

rectangle "マスタの価格" as master {
  note "現在の標準価格\n顧客別販売単価" as n1
}

rectangle "トランザクションの価格" as trans {
  note "受注時点の価格を記録\n明細に単価を保持" as n2
}

master --> trans : "受注時に複製"

note bottom of trans
  履歴として機能
  マスタ変更の影響を受けない
end note

@enduml
```

| 方式 | 説明 | 本システムの採用 |
|------|------|------------------|
| 現在価格のみ | マスタは最新の価格のみ保持 | 採用 |
| 履歴テーブル | 価格変更を別テーブルで管理 | 不採用 |
| 有効期間管理 | 開始日・終了日で有効期間を管理 | 不採用 |

本システムでは、受注明細に受注時点の単価を保持することで、事実上の価格履歴を実現しています。

### 代替商品と部品表

商品間の関連として、代替商品と部品表（BOM）を管理します。

```plantuml
@startuml
title 商品間の関連

entity "商品マスタ" as prod {
  * 商品コード [PK]
  --
  商品正式名
  ...
}

entity "代替商品" as alt {
  * 商品コード [PK][FK]
  * 代替商品コード [PK]
  --
  優先順位
  ...
}

entity "部品表" as bom {
  * 商品コード [PK][FK]
  * 部品コード [PK]
  --
  部品数量
  ...
}

prod ||--o{ alt : "代替"
prod ||--o{ bom : "構成"

@enduml
```

#### 代替商品テーブル

```sql
create table if not exists 代替商品
(
    商品コード     varchar(16)                       not null
        references 商品マスタ
        on update cascade on delete restrict,
    代替商品コード varchar(16)                       not null,
    優先順位       integer      default 1,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12),
    constraint pk_alternate_products
        primary key (商品コード, 代替商品コード)
);
```

#### 部品表テーブル

```sql
create table if not exists 部品表
(
    商品コード varchar(16)                       not null
        references 商品マスタ
        on update cascade on delete restrict,
    部品コード varchar(16)                       not null,
    部品数量   integer      default 1            not null,
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12),
    constraint pk_bom
        primary key (商品コード, 部品コード)
);
```

---

## 5.3 取引先マスタ（パーティモデル）

### 顧客と仕入先の統合設計

本システムでは、パーティモデルを採用して取引先を統合管理しています。

```plantuml
@startuml
title パーティモデルの構造

entity "取引先マスタ" as partner {
  * 取引先コード [PK]
  --
  取引先名
  取引先名カナ
  仕入先区分
  郵便番号
  都道府県
  住所１
  住所２
  取引禁止フラグ
  雑区分
  取引先グループコード [FK]
  与信限度額
  与信一時増加枠
  ...
}

entity "顧客マスタ" as customer {
  * 顧客コード [PK][FK]
  * 顧客枝番 [PK]
  --
  顧客区分
  請求先コード
  請求先枝番
  回収先コード
  回収先枝番
  顧客名
  自社担当者コード
  ...
}

entity "仕入先マスタ" as vendor {
  * 仕入先コード [PK][FK]
  * 仕入先枝番 [PK]
  --
  仕入先名
  仕入先名カナ
  仕入先担当者名
  仕入先部門名
  ...
}

partner ||--o| customer : "顧客役割"
partner ||--o| vendor : "仕入先役割"

note right of partner
  共通属性を保持
  取引先の基本情報
end note

note right of customer
  顧客固有の属性
  請求・回収情報
end note

note right of vendor
  仕入先固有の属性
  支払情報
end note

@enduml
```

#### パーティモデルのメリット

| 観点 | メリット |
|------|----------|
| データ整合性 | 取引先の基本情報が一元管理される |
| 拡張性 | 新しい役割（例：協力会社）の追加が容易 |
| 柔軟性 | 同一企業が顧客と仕入先の両方になれる |
| 重複排除 | 住所等の共通情報の重複を防止 |

#### 取引先マスタのテーブル定義

```sql
create table if not exists 取引先マスタ
(
    取引先コード         varchar(8)                        not null
        constraint pk_companys_mst primary key,
    取引先名             varchar(40)                       not null,
    取引先名カナ         varchar(40),
    仕入先区分           integer      default 0,
    郵便番号             char(8),
    都道府県             varchar(4),
    住所１                varchar(40),
    住所２                varchar(40),
    取引禁止フラグ       integer      default 0,
    雑区分               integer      default 0,
    取引先グループコード varchar(4)                        not null,
    与信限度額           integer      default 0,
    与信一時増加枠       integer      default 0,
    作成日時             timestamp(6) default CURRENT_DATE not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_DATE not null,
    更新者名             varchar(12),
    version              integer      default 0
);
```

### 取引先グループと分類

取引先を様々な観点でグルーピングするための構造です。

```plantuml
@startuml
title 取引先のグルーピング構造

entity "取引先グループマスタ" as grp {
  * 取引先グループコード [PK]
  --
  取引先グループ名
  ...
}

entity "取引先マスタ" as partner {
  * 取引先コード [PK]
  --
  取引先グループコード [FK]
  ...
}

entity "取引先分類種別マスタ" as cat_type {
  * 取引先分類種別コード [PK]
  --
  取引先分類種別名
  ...
}

entity "取引先分類マスタ" as cat {
  * 取引先分類種別コード [PK][FK]
  * 取引先分類コード [PK]
  --
  取引先分類名
  ...
}

entity "取引先分類所属マスタ" as cat_belong {
  * 取引先分類種別コード [PK]
  * 取引先分類コード [PK]
  * 取引先コード [PK][FK]
  --
  ...
}

grp ||--o{ partner : "グループ"
cat_type ||--o{ cat : "分類種別"
cat ||--o{ cat_belong : "所属"
partner ||--o{ cat_belong : "分類"

note bottom of grp
  固定のグルーピング
  （法人格、取引形態など）
end note

note bottom of cat_type
  分類の種類を定義
  （業種、地域、規模など）
end note

@enduml
```

#### 分類の使い分け

| 分類方式 | 用途 | 例 |
|----------|------|-----|
| 取引先グループ | 固定的な1対1の分類 | 法人格（株式会社、有限会社） |
| 取引先分類 | 多対多の柔軟な分類 | 業種、地域、取引形態 |

### ロール（役割）による拡張性

取引先は複数の役割を持つことができます。

```plantuml
@startuml
title 取引先の役割

package "取引先: ABC商事" {
  rectangle "顧客役割" as cust {
    note "製品を販売する相手" as n1
  }
  rectangle "仕入先役割" as vendor {
    note "部品を調達する相手" as n2
  }
}

note bottom of "取引先: ABC商事"
  同一企業が複数の役割を持てる
  例：商社は販売先かつ仕入先
end note

@enduml
```

#### 顧客マスタのテーブル定義

```sql
create table if not exists 顧客マスタ
(
    顧客コード         varchar(8)                        not null
        references 取引先マスタ
        on update cascade on delete restrict,
    顧客枝番           integer                           not null,
    顧客区分           integer      default 0,
    請求先コード       varchar(8)                        not null,
    請求先枝番         integer,
    回収先コード       varchar(8)                        not null,
    回収先枝番         integer,
    顧客名             varchar(40)                       not null,
    顧客名カナ         varchar(40),
    自社担当者コード   varchar(10)                       not null,
    顧客担当者名       varchar(20),
    顧客部門名         varchar(40),
    顧客郵便番号       char(8),
    顧客都道府県       varchar(4),
    顧客住所１          varchar(40),
    顧客住所２          varchar(40),
    顧客電話番号       varchar(13),
    "顧客ＦＡＸ番号"      varchar(13),
    顧客メールアドレス varchar(100),
    顧客請求区分       integer      default 0,
    顧客締日１          integer                           not null,
    顧客支払月１        integer      default 1,
    顧客支払日１        integer,
    顧客支払方法１      integer      default 1,
    顧客締日２          integer                           not null,
    顧客支払月２        integer      default 1,
    顧客支払日２        integer,
    顧客支払方法２      integer      default 1,
    作成日時           timestamp(6) default CURRENT_DATE not null,
    作成者名           varchar(12),
    更新日時           timestamp(6) default CURRENT_DATE not null,
    更新者名           varchar(12),
    constraint pk_customer
        primary key (顧客コード, 顧客枝番)
);
```

#### 仕入先マスタのテーブル定義

```sql
create table if not exists 仕入先マスタ
(
    仕入先コード         varchar(8)                        not null
        references 取引先マスタ
        on update cascade on delete restrict,
    仕入先枝番           integer                           not null,
    仕入先名             varchar(40)                       not null,
    仕入先名カナ         varchar(40),
    仕入先担当者名       varchar(20),
    仕入先部門名         varchar(40),
    仕入先郵便番号       char(8),
    仕入先都道府県       varchar(4),
    仕入先住所１          varchar(40),
    仕入先住所２          varchar(40),
    仕入先電話番号       varchar(13),
    "仕入先ＦＡＸ番号"      varchar(13),
    仕入先メールアドレス varchar(100),
    仕入先締日           integer                           not null,
    仕入先支払月         integer      default 1,
    仕入先支払日         integer,
    仕入先支払方法       integer      default 1,
    作成日時             timestamp(6) default CURRENT_DATE not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_DATE not null,
    更新者名             varchar(12),
    constraint pk_supplier
        primary key (仕入先コード, 仕入先枝番)
);
```

### 出荷先の管理

顧客には複数の出荷先を設定できます。

```plantuml
@startuml
title 顧客と出荷先の関連

entity "顧客マスタ" as customer {
  * 顧客コード [PK]
  * 顧客枝番 [PK]
  --
  顧客名
  ...
}

entity "出荷先マスタ" as dest {
  * 顧客コード [PK][FK]
  * 顧客枝番 [PK][FK]
  * 出荷先番号 [PK]
  --
  出荷先名
  地域コード [FK]
  出荷先郵便番号
  出荷先住所１
  出荷先住所２
  ...
}

entity "地域マスタ" as area {
  * 地域コード [PK]
  --
  地域名
  ...
}

customer ||--o{ dest : "出荷先"
area ||--o{ dest : "地域"

@enduml
```

---

## 5.4 在庫関連マスタ

### 倉庫と棚番（ロケーション）

在庫を管理するための物理的な場所を表現します。

```plantuml
@startuml
title 倉庫と棚番の関連

entity "倉庫マスタ" as wh {
  * 倉庫コード [PK]
  --
  倉庫名
  倉庫区分
  郵便番号
  都道府県
  住所１
  住所２
  ...
}

entity "棚番マスタ" as loc {
  * 倉庫コード [PK][FK]
  * 棚番コード [PK]
  * 商品コード [PK]
  --
  ...
}

entity "倉庫部門マスタ" as wh_dept {
  * 倉庫コード [PK][FK]
  * 部門コード [PK][FK]
  * 開始日 [PK][FK]
  --
  ...
}

entity "部門マスタ" as dept {
  * 部門コード [PK]
  * 開始日 [PK]
  --
  ...
}

wh ||--o{ loc : "棚番"
wh ||--o{ wh_dept : "管理部門"
dept ||--o{ wh_dept : "倉庫"

note right of wh::倉庫区分
  N: 通常倉庫
  C: 得意先
  S: 仕入先
  D: 部門倉庫
  P: 製品倉庫
  M: 原材料倉庫
end note

@enduml
```

#### 倉庫マスタのテーブル定義

```sql
create table if not exists 倉庫マスタ
(
    倉庫コード varchar(3)                        not null
        constraint pk_wh_mst primary key,
    倉庫名     varchar(20),
    倉庫区分   varchar(1)   default 'N',
    郵便番号   char(8),
    都道府県   varchar(4),
    住所１      varchar(40),
    住所２      varchar(40),
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12)
);
```

#### 棚番マスタのテーブル定義

```sql
create table if not exists 棚番マスタ
(
    倉庫コード varchar(3)                        not null
        references 倉庫マスタ
        on update cascade on delete restrict,
    棚番コード varchar(4)                        not null,
    商品コード varchar(16)                       not null,
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12),
    constraint pk_location_mst
        primary key (倉庫コード, 棚番コード, 商品コード)
);
```

### 商品と在庫の関連

在庫データは倉庫、商品、ロット、区分の組み合わせで一意に識別されます。

```plantuml
@startuml
title 在庫データの構造

entity "倉庫マスタ" as wh {
  * 倉庫コード [PK]
  --
  倉庫名
  ...
}

entity "商品マスタ" as prod {
  * 商品コード [PK]
  --
  商品正式名
  在庫管理対象区分
  ...
}

entity "在庫データ" as stock {
  * 倉庫コード [PK][FK]
  * 商品コード [PK]
  * ロット番号 [PK]
  * 在庫区分 [PK]
  * 良品区分 [PK]
  --
  実在庫数
  有効在庫数
  最終出荷日
  ...
}

wh ||--o{ stock : "保管"
prod ||--o{ stock : "在庫"

note right of stock::在庫区分
  1: 自社在庫
  2: 預り在庫
end note

note right of stock::良品区分
  G: 良品
  F: 不良品
  U: 未検品
end note

@enduml
```

#### 在庫データのテーブル定義

```sql
create table if not exists 在庫データ
(
    倉庫コード varchar(3)                        not null
        references 倉庫マスタ
        on update cascade on delete restrict,
    商品コード varchar(16)                       not null,
    ロット番号 varchar(20)                       not null,
    在庫区分   varchar(1)   default '1'          not null,
    良品区分   varchar(1)   default 'G'          not null,
    実在庫数   integer      default 1            not null,
    有効在庫数 integer      default 1            not null,
    最終出荷日 timestamp(6),
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12),
    version    integer      default 1            not null,
    constraint pk_stock
        primary key (倉庫コード, 商品コード, ロット番号, 在庫区分, 良品区分)
);
```

#### 在庫数の種類

| 種類 | 説明 | 計算方法 |
|------|------|----------|
| 実在庫数 | 実際に倉庫にある数量 | 入庫 - 出庫 |
| 有効在庫数 | 出荷可能な数量 | 実在庫数 - 引当数 |
| 引当数 | 受注に引き当てられた数量 | 受注明細の引当数合計 |

```plantuml
@startuml
title 在庫数の関係

rectangle "実在庫数 = 100" as actual
rectangle "引当数 = 30" as allocated
rectangle "有効在庫数 = 70" as available

actual --> allocated : "受注引当"
actual --> available : "残り"

note bottom of allocated
  受注済みだが未出荷
end note

note bottom of available
  新規受注可能な数量
end note

@enduml
```

---

## まとめ

本章では、マスタデータモデルの詳細について解説しました。

- **組織マスタ**: 経路列挙モデルによる部門階層構造、社員との関連
- **商品マスタ**: 商品分類との階層関連、顧客別単価、代替商品・部品表
- **取引先マスタ**: パーティモデルによる顧客・仕入先の統合設計
- **在庫関連マスタ**: 倉庫・棚番によるロケーション管理、複合キーによる在庫識別

次章では、トランザクションデータモデルについて解説します。
