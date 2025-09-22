-- 受注データ
INSERT INTO 受注データ (
    受注番号,
    受注日,
    部門コード,
    部門開始日,
    顧客コード,
    顧客枝番,
    社員コード,
    希望納期,
    客先注文番号,
    倉庫コード,
    受注金額合計,
    消費税合計,
    備考,
    作成日時,
    作成者名,
    更新日時,
    更新者名
)
VALUES
    ('OD00000001', CURRENT_DATE, '10000', '2021-01-01 00:00:00.000000', '001', 1, 'EMP001', DATEADD(DAY, 7, CURRENT_DATE),
     'CUST-ORD-001', 'W01', 15000, 1500, '初回注文', CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('OD00000002', CURRENT_DATE, '10000', '2021-01-01 00:00:00.000000', '001', 2, 'EMP002', DATEADD(DAY, 10, CURRENT_DATE),
     'CUST-ORD-002', 'W02', 30000, 3000, '大口注文', CURRENT_DATE, '販売担当', CURRENT_DATE, '販売担当');

-- 受注データ明細
INSERT INTO 受注データ明細 (
    受注番号,
    受注行番号,
    商品コード,
    商品名,
    販売単価,
    受注数量,
    消費税率,
    引当数量,
    出荷指示数量,
    出荷済数量,
    完了フラグ,
    値引金額,
    納期,
    作成日時,
    作成者名,
    更新日時,
    更新者名
)
VALUES
    ('OD00000001', 1, '10101001', '牛ひれ', 1000, 5, 10, 0, 0, 0, 0, 0, DATEADD(DAY, 7, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('OD00000001', 2, '10101002', 'ロース', 1000, 3, 10, 0, 0, 0, 0, 0, DATEADD(DAY, 7, CURRENT_DATE), CURRENT_DATE, 'システム', CURRENT_DATE, 'システム'),
    ('OD00000002', 1, '10102001', 'ひれ', 1000, 6, 10, 0, 0, 0, 0, 0, DATEADD(DAY, 10, CURRENT_DATE), CURRENT_DATE, '販売担当', CURRENT_DATE, '販売担当'),
    ('OD00000002', 2, '10102002', '豚ロース', 1000, 10, 10, 0, 0, 0, 0, 0, DATEADD(DAY, 10, CURRENT_DATE), CURRENT_DATE, '販売担当', CURRENT_DATE, '販売担当');