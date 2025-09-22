create table if not exists 個人客マスタ
(
    個人客コード   varchar(16)                       not null
        constraint customer_pk
            primary key,
    姓             varchar(20)                       not null,
    名             varchar(20)                       not null,
    姓カナ         varchar(40)                       not null,
    名カナ         varchar(40)                       not null,
    "ログインＩＤ"   varchar(256)                      not null,
    メールアドレス varchar(256)                      not null,
    パスワード     varchar(16)                       not null,
    生年月日       timestamp(6)                      not null,
    性別           integer                           not null,
    ログイン日時   timestamp(6),
    ポイント残高   integer,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12),
    退会日         timestamp(6)
);

create table if not exists 仕入データ
(
    仕入番号         varchar(10)                       not null
        constraint pk_pu
            primary key,
    仕入日           timestamp(6) default CURRENT_DATE,
    仕入先コード     varchar(8)                        not null,
    仕入先枝番       integer      default 0,
    仕入担当者コード varchar(10)                       not null,
    開始日           timestamp(6) default CURRENT_DATE not null,
    発注番号         varchar(10),
    部門コード       varchar(6)                        not null,
    仕入金額合計     integer      default 0,
    消費税合計       integer      default 0            not null,
    備考             varchar(1000),
    作成日時         timestamp(6) default CURRENT_DATE not null,
    作成者名         varchar(12),
    更新日時         timestamp(6) default CURRENT_DATE not null,
    updater          varchar(12)
);

create table if not exists 仕入データ明細
(
    仕入番号       varchar(10)                       not null
        references 仕入データ
            on update cascade on delete restrict,
    仕入行番号     integer                           not null,
    仕入行表示番号 integer                           not null,
    発注行番号     integer                           not null,
    商品コード     varchar(16)                       not null,
    倉庫コード     varchar(3)                        not null,
    商品名         varchar(10)                       not null,
    仕入単価       integer      default 0,
    仕入数量       integer      default 1            not null,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12),
    constraint pk_pu_details
        primary key (仕入行番号, 仕入番号)
);

create table if not exists 支払データ
(
    支払番号       varchar(10)                       not null
        constraint pk_pay
            primary key,
    支払日         integer      default 0,
    部門コード     varchar(6)                        not null,
    部門開始日     timestamp(6) default CURRENT_DATE not null,
    仕入先コード   varchar(8)                        not null,
    仕入先枝番     integer      default 0,
    支払方法区分   integer      default 1,
    支払金額       integer      default 0,
    消費税合計     integer      default 0            not null,
    支払完了フラグ integer      default 0            not null,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12)
);

comment on column 支払データ.支払日 is '10:10日払い, 99:末日';

comment on column 支払データ.支払方法区分 is '1:振込, 2:手形';

comment on column 支払データ.支払完了フラグ is '0:未完了, 1:完了';

create table if not exists 与信残高データ
(
    取引先コード varchar(8)                        not null
        constraint pk_credit_balance
            primary key,
    受注残高     integer      default 0,
    債権残高     integer      default 0,
    債務残高     integer      default 0,
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12)
);
