ALTER TABLE 売上データ
   ADD COLUMN 顧客コード varchar(8) NOT NULL,
   ADD COLUMN 顧客枝番 integer;

ALTER TABLE 売上データ明細
    ADD COLUMN 受注番号 varchar(10) NOT NULL,
    ADD COLUMN 受注行番号 integer NOT NULL;