ALTER TABLE "仕入データ"
    ADD COLUMN "version" INT DEFAULT 0;

ALTER TABLE "仕入データ明細"
    ADD COLUMN "version" INT DEFAULT 0;
