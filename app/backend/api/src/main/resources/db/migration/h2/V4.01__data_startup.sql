-- 取引先グループマスタのサンプルデータ挿入
INSERT INTO 取引先グループマスタ (取引先グループコード, 取引先グループ名, 作成日時, 作成者名, 更新日時, 更新者名)
VALUES
    ('0001', '得意先', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('0002', '仕入先', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- 取引先マスタのサンプルデータ
insert into 取引先マスタ (取引先コード, 取引先名, 取引先名カナ, 仕入先区分, 郵便番号, 都道府県, 住所１, 住所２, 取引禁止フラグ, 雑区分, 取引先グループコード, 与信限度額, 与信一時増加枠, 作成日時, 作成者名, 更新日時, 更新者名)
values
    ('001', 'DBMフード株式会社', 'デービーエムフードカブシキガイシャ', 0, '123-4567', '東京都', '千代田区丸の内', 'ビル1F', 0, 0, '0001', 1000000, 100000, CURRENT_TIMESTAMP, '作成者A', CURRENT_TIMESTAMP, '更新者A'),
    ('002', '株式会社B', 'カブシキガイシャビー', 1, '987-6543', '大阪府', '梅田1丁目', 'タワー2F', 0, 0, '0002', 500000, 50000, CURRENT_TIMESTAMP, '作成者B', CURRENT_TIMESTAMP, '更新者B'),
    ('003', '株式会社C', 'カブシキガイシャシー', 1, '731-0001', '広島県', '広島市', '安佐南区', 0, 0, '0002', 500000, 50000, CURRENT_TIMESTAMP, '作成者B', CURRENT_TIMESTAMP, '更新者B');

-- 顧客マスタのサンプルデータ
insert into 顧客マスタ (顧客コード, 顧客枝番, 顧客区分, 請求先コード, 請求先枝番, 回収先コード, 回収先枝番, 顧客名, 顧客名カナ, 自社担当者コード, 顧客担当者名, 顧客部門名, 顧客郵便番号, 顧客都道府県, 顧客住所１, 顧客住所２, 顧客電話番号, 顧客ＦＡＸ番号, 顧客メールアドレス, 顧客請求区分, 顧客締日１, 顧客支払月１, 顧客支払日１, 顧客支払方法１, 顧客締日２, 顧客支払月２, 顧客支払日２, 顧客支払方法２, 作成日時, 作成者名, 更新日時, 更新者名)
values
    ('001', 1, 0, '001', null, '001', null, '東京本社', 'トウキョウホンシャ', 'T001', '田中太郎', '営業部', '123-4567', '東京都', '千代田区丸の内2丁目', 'ビル3F', '03-1234-5678', '03-1234-9876', 'tanaka@example.com', 2, 15, 1, 10, 1, 15, 1, 99, 1, CURRENT_TIMESTAMP, '作成者A', CURRENT_TIMESTAMP, '更新者A'),
    ('001', 2, 0, '001', null, '001', null, '関西支社', 'カンサイシシャ', 'T001', '田中太郎', '営業部', '123-4567', '大阪府', '梅田1丁目', 'ビル1F', '03-1234-5678', '03-1234-9876', 'tanaka@example.com', 2, 15, 1, 10, 1, 15, 1, 99, 1, CURRENT_TIMESTAMP, '作成者A', CURRENT_TIMESTAMP, '更新者A');

-- 出荷先マスタのサンプルデータ
insert into 出荷先マスタ (顧客コード, 顧客枝番, 出荷先番号, 出荷先名, 地域コード, 出荷先郵便番号, 出荷先住所１, 出荷先住所２, 作成日時, 作成者名, 更新日時, 更新者名)
values
    ('001', 1, 1, '新宿営業所', 'R001', '123-4567', '新宿1丁目', '倉庫A区画', CURRENT_TIMESTAMP, '作成者A', CURRENT_TIMESTAMP, '更新者A'),
    ('001', 1, 2, '横浜営業所', 'R001', '123-4567', '横浜2丁目', '倉庫B区画', CURRENT_TIMESTAMP, '作成者A', CURRENT_TIMESTAMP, '更新者A'),
    ('001', 1, 3, '中之島営業所', 'R001', '123-4567', '千代田区丸の内2丁目', '倉庫区画', CURRENT_TIMESTAMP, '作成者A', CURRENT_TIMESTAMP, '更新者A');

-- 地域マスタのサンプルデータ
INSERT INTO 地域マスタ (地域コード, 地域名, 作成日時, 作成者名, 更新日時, 更新者名)
VALUES
    ('R001', '北海道', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R002', '東北', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R003', '関東', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R004', '中部', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R005', '近畿', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R006', '中国', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R007', '四国', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者'),
    ('R008', '九州', CURRENT_TIMESTAMP, '管理者', CURRENT_TIMESTAMP, '管理者');

-- 仕入先マスタのサンプルデータ
insert into 仕入先マスタ (仕入先コード, 仕入先枝番, 仕入先名, 仕入先名カナ, 仕入先担当者名, 仕入先部門名, 仕入先郵便番号, 仕入先都道府県, 仕入先住所１, 仕入先住所２, 仕入先電話番号, 仕入先ＦＡＸ番号, 仕入先メールアドレス, 仕入先締日, 仕入先支払月, 仕入先支払日, 仕入先支払方法, 作成日時, 作成者名, 更新日時, 更新者名)
values
    ('002', 1, '本社', 'ホンシャ', '山田花子', '仕入部', '987-6543', '大阪府', '吹田市1丁目', '工場1F', '06-5432-1234', '06-5432-9876', 'yamada@example.com', 15, 1, 10, 2, CURRENT_TIMESTAMP, '作成者B', CURRENT_TIMESTAMP, '更新者B');

-- 取引先分類種別マスタへのサンプルデータ挿入
INSERT INTO 取引先分類種別マスタ (取引先分類種別コード, 取引先分類種別名, 作成日時, 作成者名, 更新日時, 更新者名)
VALUES
    ('01', '業種', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('02', '業態', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('03', '規模', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- 取引先分類マスタへのサンプルデータ挿入
INSERT INTO 取引先分類マスタ (取引先分類種別コード, 取引先分類コード, 取引先分類名, 作成日時, 作成者名, 更新日時, 更新者名)
VALUES
    ('02', '001', '小売', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('02', '002', '卸', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('02', '003', '製造', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('02', '004', 'サービス', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- 取引先分類所属マスタへのサンプルデータ挿入
INSERT INTO 取引先分類所属マスタ (取引先分類種別コード, 取引先分類コード, 取引先コード, 作成日時, 作成者名, 更新日時, 更新者名)
VALUES
    ('02', '002', '001', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('02', '002', '002', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('02', '002', '003', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
