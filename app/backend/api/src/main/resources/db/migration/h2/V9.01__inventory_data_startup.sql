-- 倉庫マスタ
INSERT INTO 倉庫マスタ (
    倉庫コード,
    倉庫名,
    倉庫区分,
    郵便番号,
    都道府県,
    住所１,
    住所２,
    作成日時,
    作成者名,
    更新日時,
    更新者名
)
VALUES
    ('W01', '第一倉庫', 'N', '100-0001', '東京都', '千代田区丸の内1-1-1', 'ビルA', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', '第二倉庫', 'N', '100-0002', '東京都', '千代田区丸の内2-2-2', 'ビルB', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W03', '冷凍倉庫', 'P', '100-0003', '東京都', '千代田区丸の内3-3-3', '冷蔵施設C', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W10', '仕入先倉庫', 'S', '200-0001', '神奈川県', '横浜市西区1-1-1', '配送センター', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W20', '得意先倉庫', 'C', '300-0001', '千葉県', '千葉市美浜区1-1-1', '営業所倉庫', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム');

-- 棚番マスタ
INSERT INTO 棚番マスタ (
    倉庫コード,
    棚番コード,
    商品コード,
    作成日時,
    作成者名,
    更新日時,
    更新者名
)
VALUES
    -- 第一倉庫（WH1）の棚番
    ('W01', 'A001', '10101001', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', 'A002', '10101002', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', 'B001', '10103001', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', 'B002', '10103002', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', 'B003', '10103003', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 第二倉庫（WH2）の棚番
    ('W02', 'C001', '10102001', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', 'C002', '10102002', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', 'D001', '90101001', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', 'D002', '90101002', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', 'D003', '90101003', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', 'D004', '90101004', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 冷凍倉庫（WH3）の棚番
    ('W03', 'F001', '10104001', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W03', 'F002', '10104002', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W03', 'F003', '10104003', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム');

-- 在庫データ
INSERT INTO 在庫データ (
    倉庫コード,
    商品コード,
    ロット番号,
    在庫区分,
    良品区分,
    実在庫数,
    有効在庫数,
    最終出荷日,
    作成日時,
    作成者名,
    更新日時,
    更新者名
)
VALUES
    -- 第一倉庫（WH1）の在庫（牛肉類・野菜類）
    ('W01', '10101001', 'LOT202501001', '1', 'G', 50, 45, DATEADD(DAY, -2, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', '10101002', 'LOT202501002', '1', 'G', 30, 25, DATEADD(DAY, -1, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', '10103001', 'LOT202501003', '1', 'G', 100, 95, DATEADD(DAY, -3, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', '10103002', 'LOT202501004', '1', 'G', 80, 75, DATEADD(DAY, -2, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W01', '10103003', 'LOT202501005', '1', 'G', 120, 110, DATEADD(DAY, -4, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 不良品在庫の例
    ('W01', '10101001', 'LOT202501001', '1', 'B', 5, 0, NULL, CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 第二倉庫（WH2）の在庫（豚肉類・調味料類）
    ('W02', '10102001', 'LOT202501006', '1', 'G', 60, 55, DATEADD(DAY, -1, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', '10102002', 'LOT202501007', '1', 'G', 40, 35, DATEADD(DAY, -3, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', '90101001', 'LOT202501008', '1', 'G', 200, 190, DATEADD(DAY, -5, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', '90101002', 'LOT202501009', '1', 'G', 150, 145, DATEADD(DAY, -4, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', '90101003', 'LOT202501010', '1', 'G', 300, 280, DATEADD(DAY, -6, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W02', '90101004', 'LOT202501011', '1', 'G', 250, 240, DATEADD(DAY, -3, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 預り在庫の例
    ('W02', '90101001', 'LOT202501012', '2', 'G', 50, 50, NULL, CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 冷凍倉庫（WH3）の在庫（冷凍品）
    ('W03', '10104001', 'LOT202501013', '1', 'G', 20, 15, DATEADD(DAY, -1, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W03', '10104002', 'LOT202501014', '1', 'G', 35, 30, DATEADD(DAY, -2, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('W03', '10104003', 'LOT202501015', '1', 'G', 15, 10, DATEADD(DAY, -3, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),

    -- 未検品在庫の例
    ('W03', '10104001', 'LOT202501016', '1', 'R', 10, 0, NULL, CURRENT_DATE, 'システム', CURRENT_DATE, 'システム');

-- 倉庫部門マスタ（テスト環境では一時的にコメントアウト）
-- V2.01で既存の部門マスタとの整合性を確保するため、倉庫部門マスタの挿入は一時的に無効化
-- INSERT INTO 倉庫部門マスタ (
--     倉庫コード,
--     部門コード,
--     開始日,
--     作成日時,
--     作成者名,
--     更新日時,
--     更新者名
-- )
-- VALUES
--     -- 第一倉庫（WH1）の部門割り当て
--     ('W01', '11101', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--     ('W01', '11102', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--
--     -- 第二倉庫（WH2）の部門割り当て
--     ('W02', '11203', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--     ('W02', '11204', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--     ('W02', '12101', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--
--     -- 冷凍倉庫（WH3）の部門割り当て
--     ('W03', '12101', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--     ('W03', '12203', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--
--     -- 仕入先倉庫（WHS）の部門割り当て
--     ('W04', '11101', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
--
--     -- 得意先倉庫（WHC）の部門割り当て
--     ('W04', '11203', '2021-01-01 00:00:00.000000', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム');
