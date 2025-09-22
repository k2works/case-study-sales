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
    version          integer      default 1            not null,
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

