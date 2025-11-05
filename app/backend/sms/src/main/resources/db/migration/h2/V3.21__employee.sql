-- user_idカラムを追加
drop table if exists 社員マスタ;
create table if not exists 社員マスタ
(
    社員コード     varchar(10)                       not null
    constraint pk_employee
    primary key,
    社員名         varchar(20),
    社員名カナ     varchar(40),
    パスワード     varchar(8),
    電話番号       varchar(13),
    fax番号       varchar(13),
    部門コード     varchar(6)                        not null,
    開始日         timestamp(6) default CURRENT_DATE not null,
    職種コード     varchar(2)                        not null,
    承認権限コード varchar(2)                        not null,
    作成日時       timestamp(6) default CURRENT_DATE not null,
    作成者名       varchar(12),
    更新日時       timestamp(6) default CURRENT_DATE not null,
    更新者名       varchar(12),
    user_id        varchar(255)
    );

