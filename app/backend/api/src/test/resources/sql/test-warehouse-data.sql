-- テスト用倉庫マスタデータ
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
) VALUES
    ('WH1', '第一倉庫', 'N', '100-0001', '東京都', '千代田区丸の内1-1-1', 'ビルA', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('WH2', '第二倉庫', 'N', '100-0002', '東京都', '千代田区丸の内2-2-2', 'ビルB', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('WH3', '冷凍倉庫', 'P', '100-0003', '東京都', '千代田区丸の内3-3-3', '冷蔵施設C', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system')
ON CONFLICT (倉庫コード) DO NOTHING;