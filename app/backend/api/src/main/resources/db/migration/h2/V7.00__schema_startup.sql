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
    顧客コード   varchar(8)                        not null,
    顧客枝番     integer,
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

