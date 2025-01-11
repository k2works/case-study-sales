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
    更新者名             varchar(12),
    version         integer      default 0
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
    更新者名             varchar(12),
    version         integer      default 0
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

