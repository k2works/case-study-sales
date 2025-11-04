-- versionカラムを追加
drop table if exists 顧客別販売単価;
drop table if exists 代替商品;
drop table if exists 部品表;

drop table if exists 商品マスタ;
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
    更新者名         varchar(12),
    version         integer      default 0
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

