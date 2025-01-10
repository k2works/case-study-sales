create table if not exists 取引先マスタ
(
    取引先コード         varchar(8)                        not null
        constraint pk_companys_mst
            primary key,
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
    更新者名             varchar(12)
);

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

comment on column 顧客マスタ.顧客請求区分 is '1:都度請求 2:締請求';

comment on column 顧客マスタ.顧客締日１ is '15:15日締め';

comment on column 顧客マスタ.顧客支払月１ is '0:当月 1:翌月 2:翌々月';

comment on column 顧客マスタ.顧客支払日１ is '10:10日払い 99:末日';

comment on column 顧客マスタ.顧客支払方法１ is '1:振込 2:手形';

comment on column 顧客マスタ.顧客締日２ is '15:15日締め';

comment on column 顧客マスタ.顧客支払月２ is '0:当月 1:翌月 2:翌々月';

comment on column 顧客マスタ.顧客支払日２ is '10:10日払い 99:末日';

comment on column 顧客マスタ.顧客支払方法２ is '1:振込 2:手形';

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

comment on column 仕入先マスタ.仕入先締日 is '15:15日締め';

comment on column 仕入先マスタ.仕入先支払月 is '0:当月 1:翌月 2:翌々月';

comment on column 仕入先マスタ.仕入先支払日 is '10:10日払い 99:末日';

comment on column 仕入先マスタ.仕入先支払方法 is '1:振込 2:手形';

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

create table if not exists 取引先グループマスタ
(
    取引先グループコード varchar(4)                        not null
        constraint pk_company_group_mst
            primary key,
    取引先グループ名     varchar(40),
    作成日時             timestamp(6) default CURRENT_DATE not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_DATE not null,
    更新者名             varchar(12)
);

create table if not exists 取引先分類種別マスタ
(
    取引先分類種別コード varchar(2)                        not null
        constraint pk_category_type
            primary key,
    取引先分類種別名     varchar(20),
    作成日時             timestamp(6) default CURRENT_DATE not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_DATE not null,
    更新者名             varchar(12)
);

create table if not exists 取引先分類マスタ
(
    取引先分類種別コード varchar(2)                        not null
        references 取引先分類種別マスタ
            on update cascade on delete restrict,
    取引先分類コード     varchar(8)                        not null,
    取引先分類名         varchar(30),
    作成日時             timestamp(6) default CURRENT_DATE not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_DATE not null,
    更新者名             varchar(12),
    constraint pk_company_category
        primary key (取引先分類種別コード, 取引先分類コード)
);

create table if not exists 取引先分類所属マスタ
(
    取引先分類種別コード varchar(2)                        not null,
    取引先分類コード     varchar(8)                        not null,
    取引先コード         varchar(8)                        not null
        references 取引先マスタ(取引先コード)
            on update cascade on delete restrict,
    作成日時             timestamp(6) default CURRENT_TIMESTAMP not null,
    作成者名             varchar(12),
    更新日時             timestamp(6) default CURRENT_TIMESTAMP not null,
    更新者名             varchar(12),
    constraint pk_company_category_group
        primary key (取引先分類種別コード, 取引先コード, 取引先分類コード),
    constraint 取引先分類所属マスタ_取引先分類コード__fkey
        foreign key (取引先分類コード, 取引先分類種別コード) references 取引先分類マスタ(取引先分類コード, 取引先分類種別コード)
            on update cascade on delete restrict
);

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
    更新者名     varchar(12)
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
    作成日時     timestamp(6) default CURRENT_DATE not null,
    作成者名     varchar(12),
    更新日時     timestamp(6) default CURRENT_DATE not null,
    更新者名     varchar(12),
    primary key (受注番号, 受注行番号)
);

comment on column 受注データ明細.完了フラグ is '0:未完了, 1:完了';

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
    更新者名     varchar(12)
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

create table if not exists 出荷先マスタ
(
    顧客コード     varchar(8)                        not null,
    顧客枝番       integer                           not null,
    出荷先番号     integer                           not null,
    出荷先名       varchar(40)                       not null,
    地域コード     varchar(10)                       not null,
    出荷先郵便番号 char(8),
    出荷先住所１    varchar(40),
    出荷先住所２    varchar(40),
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12),
    constraint pk_destinations
        primary key (顧客コード, 出荷先番号, 顧客枝番),
    foreign key (顧客コード, 顧客枝番) references 顧客マスタ
        on update cascade on delete restrict
);

create table if not exists 地域マスタ
(
    地域コード varchar(10)                       not null
        constraint pk_area
            primary key,
    地域名     varchar(20),
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12)
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

create table if not exists 自動採番マスタ
(
    伝票種別コード varchar(2)        not null,
    年月           timestamp(6)      not null,
    最終伝票番号   integer default 0 not null,
    primary key (伝票種別コード, 年月)
);
