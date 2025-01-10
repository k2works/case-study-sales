create table if not exists 部門マスタ
(
    部門コード   varchar(6)                        not null,
    開始日       timestamp(6) default CURRENT_DATE not null,
    終了日       timestamp(6) default '2100-12-31 00:00:00'::timestamp without time zone,
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

comment on column 部門マスタ.伝票入力可否 is '0:不可 1:可能';

create table if not exists 社員マスタ
(
    社員コード     varchar(10)                       not null
    constraint pk_employee
    primary key,
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

create table if not exists 商品分類マスタ
(
    商品分類コード varchar(8)                        not null
    constraint pk_product_category
    primary key,
    商品分類名     varchar(30),
    商品分類階層   integer      default 0            not null,
    商品分類パス   varchar(100),
    最下層区分     integer      default 0,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12)
    );

create table if not exists 商品マスタ
(
    商品コード       varchar(16)                       not null
    constraint pk_products
    primary key,
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

comment on column 商品マスタ.商品区分 is '1:商品 2:製品 3:原材料 4:間接材';

comment on column 商品マスタ.在庫管理対象区分 is '0:対象外 1:在庫管理対象';

comment on column 商品マスタ.在庫引当区分 is '0:対象外 1:即時 2:まとめ 3:手配品';

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

comment on column 倉庫マスタ.倉庫区分 is 'N:通常倉庫, C:得意先, S:仕入先, D:部門倉庫, P:製品倉庫, M:原材料倉庫';

create table if not exists 倉庫部門マスタ
(
    倉庫コード varchar(3)                        not null
    references 倉庫マスタ
    on update cascade on delete restrict,
    部門コード varchar(6)                        not null,
    開始日     timestamp(6) default CURRENT_DATE not null,
    作成日時   timestamp(6) default CURRENT_DATE not null,
    作成者名   varchar(12),
    更新日時   timestamp(6) default CURRENT_DATE not null,
    更新者名   varchar(12),
    constraint pk_wh_dept_mst
    primary key (倉庫コード, 部門コード, 開始日),
    foreign key (部門コード, 開始日) references 部門マスタ
    on update cascade on delete restrict
    );

