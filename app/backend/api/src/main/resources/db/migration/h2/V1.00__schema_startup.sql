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

create table if not exists 入金口座マスタ
(
    入金口座コード       varchar(8)                        not null
        constraint pk_bank_acut_mst
            primary key,
    入金口座名           varchar(30),
    適用開始日           timestamp(6) default CURRENT_DATE not null,
    適用終了日           timestamp(6) default '2100-12-31 00:00:00'::timestamp without time zone,
    適用開始後入金口座名 varchar(30),
    入金口座区分         varchar(1),
    入金口座番号         varchar(12),
    銀行口座種別         varchar(1),
    口座名義人           varchar(20),
    部門コード           varchar(6)                        not null,
    部門開始日           timestamp(6) default CURRENT_DATE not null,
    全銀協銀行コード     varchar(4),
    全銀協支店コード     varchar(3),
    作成日時             timestamp(6) default CURRENT_DATE not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_DATE not null,
    更新者名             varchar(12),
    プログラム更新日時   timestamp(6) default CURRENT_DATE,
    更新プログラム名     varchar(50)
);

comment on column 入金口座マスタ.入金口座区分 is 'B:銀行, P:郵便局';

comment on column 入金口座マスタ.入金口座番号 is '銀行:7桁 郵便局:12桁';

comment on column 入金口座マスタ.銀行口座種別 is 'O:普通 C:当座';

create table if not exists 入金データ
(
    入金番号           varchar(10)                       not null
        constraint pk_credit
            primary key,
    入金日             timestamp(6),
    部門コード         varchar(6)                        not null,
    開始日             timestamp(6) default CURRENT_DATE not null,
    顧客コード         varchar(8)                        not null,
    顧客枝番           integer      default 0,
    支払方法区分       integer      default 1,
    入金口座コード     varchar(8),
    入金金額           integer      default 0,
    消込金額           integer      default 0,
    作成日時           timestamp(6) default CURRENT_DATE not null,
    作成者名           varchar(12),
    更新日時           timestamp(6) default CURRENT_DATE not null,
    更新者名           varchar(12),
    プログラム更新日時 timestamp(6) default CURRENT_DATE,
    更新プログラム名   varchar(50)
);

comment on column 入金データ.支払方法区分 is '1:振込 2:手形';

create table if not exists 発注データ
(
    発注番号         varchar(10)                       not null
        constraint pk_purchase_orders
            primary key,
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
    更新者名         varchar(12)
);

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
    constraint pk_purchase_order_details
        primary key (発注行番号, 発注番号)
);

comment on column 発注データ明細.完了フラグ is '0:未完了 1:完了';

create table if not exists 倉庫マスタ
(
    倉庫コード varchar(3)                        not null
        constraint pk_wh_mst
            primary key,
    倉庫名     varchar(20),
    倉庫区分   varchar(1)   default 'N'::character varying,
    郵便番号   char(8),
    都道府県   varchar(4),
    住所１      varchar(40),
    住所２      varchar(40),
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12)
);

comment on column 倉庫マスタ.倉庫区分 is 'N:通常倉庫, C:得意先, S:仕入先, D:部門倉庫, P:製品倉庫, M:原材料倉庫';

create table if not exists 在庫データ
(
    倉庫コード varchar(3)                                  not null
        references 倉庫マスタ
            on update cascade on delete restrict,
    商品コード varchar(16)                                 not null,
    ロット番号 varchar(20)                                 not null,
    在庫区分   varchar(1)   default '1'::character varying not null,
    良品区分   varchar(1)   default 'G'::character varying not null,
    実在庫数   integer      default 1                      not null,
    有効在庫数 integer      default 1                      not null,
    最終出荷日 timestamp(6),
    作成日時   timestamp(6) default CURRENT_DATE           not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE           not null,
    更新者名   varchar(12),
    constraint pk_stock
        primary key (倉庫コード, 商品コード, ロット番号, 在庫区分, 良品区分)
);

comment on column 在庫データ.在庫区分 is '1:自社在庫, 2:預り在庫,';

comment on column 在庫データ.良品区分 is 'G:良品, F:不良品, U:未検品';

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
