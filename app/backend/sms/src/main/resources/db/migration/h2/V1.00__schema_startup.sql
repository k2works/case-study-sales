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
