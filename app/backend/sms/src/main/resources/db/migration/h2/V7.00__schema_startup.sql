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

