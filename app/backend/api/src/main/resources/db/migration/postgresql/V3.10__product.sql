-- user_idカラムを追加
ALTER TABLE "商品マスタ"
    ADD COLUMN "version" INT DEFAULT 0;
