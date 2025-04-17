create table if not exists 売上データ
(
    売上番号     varchar(10)                       not null
        constraint pk_sales
            primary key,
    受注番号     varchar(10)                       not null,
    売上日       timestamp(6) default CURRENT_DATE not null,
    売上区分     integer      default 1,
    部門コード   varchar(6)                        not null,
    部門開始日   timestamp(6) default CURRENT_DATE not null,
    取引先コード varchar(8)                        not null,
    社員コード   varchar(10)                       not null,
    売上金額合計 integer      default 0            not null,
    消費税合計   integer      default 0            not null,
    備考         varchar(1000),
    赤黒伝票番号 integer,
    元伝票番号   varchar(10),
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    version         integer      default 0
);

create table if not exists 売上データ明細
(
    売上番号     varchar(10)                       not null
        references 売上データ
            on update cascade on delete restrict,
    売上行番号   integer                           not null,
    商品コード   varchar(16)                       not null,
    商品名       varchar(10)                       not null,
    販売単価     integer      default 0            not null,
    出荷数量     integer      default 0,
    売上数量     integer      default 1            not null,
    値引金額     integer      default 0            not null,
    消費税率     integer      default 0,
    請求日       timestamp(6),
    請求番号     varchar(10),
    請求遅延区分 integer,
    自動仕訳日   timestamp(6),
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    constraint pk_sales_details
        primary key (売上番号, 売上行番号)
);

create table if not exists 自動採番マスタ
(
    伝票種別コード varchar(2)        not null,
    年月           timestamp(6)      not null,
    最終伝票番号   integer default 0 not null,
    primary key (伝票種別コード, 年月)
);

create table if not exists 請求データ
(
    請求番号     varchar(10)                       not null
    constraint pk_invoice
    primary key,
    請求日       timestamp(6),
    取引先コード varchar(8)                        not null,
    顧客枝番     integer      default 0,
    前回入金額   integer      default 0,
    当月売上額   integer      default 0,
    当月入金額   integer      default 0,
    当月請求額   integer      default 0,
    消費税金額   integer      default 0            not null,
    請求消込金額 integer      default 0,
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12)
    );

create table if not exists 請求データ明細
(
    請求番号   varchar(10)                       not null
    references 請求データ
    on update cascade on delete restrict,
    売上番号   varchar(10)                       not null,
    売上行番号 integer                           not null,
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12),
    constraint pk_invoice_details
    primary key (請求番号, 売上番号, 売上行番号),
    foreign key (売上番号, 売上行番号) references 売上データ明細
    on update cascade on delete restrict
    );
