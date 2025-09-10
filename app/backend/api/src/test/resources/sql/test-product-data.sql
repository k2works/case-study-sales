-- テスト用商品マスタデータ  
INSERT INTO 商品マスタ (
    商品コード,
    商品正式名,
    商品略称,
    商品名カナ,
    商品区分,
    商品分類コード,
    販売単価,
    仕入単価,
    売上原価,
    税区分,
    仕入先コード,
    仕入先枝番,
    作成日時,
    作成者名,
    更新日時,
    更新者名
) VALUES
    ('10101001', '牛ひれ', '牛ひれ', 'ギュウヒレ', '1', '101', 1200, 800, 800, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('10101002', '牛ロース', '牛ロース', 'ギュウロース', '1', '101', 1000, 700, 700, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('10102001', '豚ロース', '豚ロース', 'ブタロース', '1', '102', 800, 500, 500, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('10102002', '豚バラ', '豚バラ', 'ブタバラ', '1', '102', 700, 400, 400, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('10103001', 'キャベツ', 'キャベツ', 'キャベツ', '1', '103', 200, 100, 100, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('10103002', 'にんじん', 'にんじん', 'ニンジン', '1', '103', 150, 80, 80, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('10103003', 'たまねぎ', 'たまねぎ', 'タマネギ', '1', '103', 100, 50, 50, 1, '001', 0, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system')
ON CONFLICT (商品コード) DO NOTHING;