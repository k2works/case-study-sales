create table if not exists 受注データ
(
    受注番号     varchar(10)                       not null
    constraint pk_orders
    primary key,
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
    version         integer      default 0
    );

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

comment on column 受注データ明細.完了フラグ is '0:未完了, 1:完了';
