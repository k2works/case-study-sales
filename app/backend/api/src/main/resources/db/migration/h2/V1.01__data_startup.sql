-- 部門マスタ
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('10000', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '全社', 0, '10000~', 0, 1,
        '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11000', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '営業本部', 1, '10000~11000~', 0, 1,
        '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11100', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '東日本営業部', 2, '10000~11000~11100~', 0,
        1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11101', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '営業１課', 3, '10000~11000~11100~11101~',
        1, 1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11102', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '営業２課', 3, '10000~11000~11100~11102~',
        1, 1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11200', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '西日本営業部', 2, '10000~11000~11200~', 1,
        1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11203', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '営業３課', 3, '10000~11000~11200~11103~',
        1, 1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('11204', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '営業４課', 3, '10000~11000~11200~11104~',
        1, 1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12000', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '管理本部', 1, '10000~12000~', 0, 1,
        '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12100', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '総務部', 2, '10000~12000~12100~', 0, 1,
        '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12101', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '庶務２課', 3, '10000~12000~12100~12101~',
        0, 1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12102', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '人事課', 3, '10000~12000~12100~12102~', 1,
        1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12200', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '経理部', 2, '10000~12000~12200~', 0, 1,
        '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12203', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '経理課', 3, '10000~12000~12200~12203~', 1,
        1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
INSERT INTO public."部門マスタ" ("部門コード", "開始日", "終了日", "部門名", "組織階層", "部門パス", "最下層区分",
                                 "伝票入力可否", "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('12204', '2021-01-01 00:00:00.000000', '9999-12-31 00:00:00.000000', '財務課', 3, '10000~12000~12200~12204~', 1,
        1, '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000', 'admin');
-- 社員マスタ
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP999', '伊藤 裕子', 'イトウ ユウコ', 'password', '090-1234-5678', '03-1234-5678', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2021-01-01 00:00:00.000000', 'admin', '2021-01-01 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP001', '山田 太郎', 'ヤマダ タロウ', 'password', '090-1234-5678', '03-1234-5678', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP002', '佐藤 花子', 'サトウ ハナコ', 'password', '090-2345-6789', '03-2345-6789', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP003', '鈴木 一郎', 'スズキ イチロウ', 'password', '090-3456-7890', '03-3456-7890', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP004', '田中 久美子', 'タナカ クミコ', 'password', '090-4567-8901', '03-4567-8901', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP005', '渡辺 健太', 'ワタナベ ケンタ', 'password', '090-5678-9012', '03-5678-9012', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP006', '小林 美香', 'コバヤシ ミカ', 'password', '090-6789-0123', '03-6789-0123', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP007', '山本 健一郎', 'ヤマモト ケンイチロウ', 'password', '090-7890-1234', '03-7890-1234', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP008', '中村 さやか', 'ナカムラ サヤカ', 'password', '090-8901-2345', '03-8901-2345', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP009', '加藤 純一', 'カトウ ジュンイチ', 'password', '090-9012-3456', '03-9012-3456', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP010', '吉田 由美子', 'ヨシダ ユミコ', 'password', '090-0123-4567', '03-0123-4567', '11101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP011', '山田 太郎', 'ヤマダ タロウ', 'password', '090-1234-5678', '03-1234-5678', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP012', '佐藤 一郎', 'サトウ イチロウ', 'password', '090-2345-6789', '03-2345-6789', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP013', '田中 次郎', 'タナカ ジロウ', 'password', '090-3456-7890', '03-3456-7890', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP014', '鈴木 三郎', 'スズキ サブロウ', 'password', '090-4567-8901', '03-4567-8901', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP015', '高橋 四郎', 'タカハシ シロウ', 'password', '090-5678-9012', '03-5678-9012', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP016', '伊藤 五郎', 'イトウ ゴロウ', 'password', '090-6789-0123', '03-6789-0123', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP017', '渡辺 六郎', 'ワタナベ ロクロウ', 'password', '090-7890-1234', '03-7890-1234', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP018', '木村 七郎', 'キムラ シチロウ', 'password', '090-8901-2345', '03-8901-2345', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP019', '山本 八郎', 'ヤマモト ハチロウ', 'password', '090-9012-3456', '03-9012-3456', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP020', '中村 九郎', 'ナカムラ クロウ', 'password', '090-0123-4567', '03-0123-4567', '11102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP021', '山田 太郎', 'やまだ たろう', 'password', '090-1234-5678', '03-1234-5678', '11203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP022', '佐藤 花子', 'さとう はなこ', 'password', '090-2345-6789', '03-2345-6789', '11203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP023', '田中 一郎', 'たなか いちろう', 'password', '090-3456-7890', '03-3456-7890', '11203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP024', '鈴木 朋子', 'すずき ともこ', 'password', '090-4567-8901', '03-4567-8901', '11203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP025', '高橋 勇介', 'たかはし ゆうすけ', 'password', '090-5678-9012', '03-5678-9012', '11203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP026', '山田 太郎', 'ヤマダ タロウ', 'password', '090-1234-5678', '03-1234-5678', '11204',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP027', '佐藤 一郎', 'サトウ イチロウ', 'password', '080-9876-5432', '03-1111-2222', '11204',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP028', '田中 二郎', 'タナカ ジロウ', 'password', '090-1111-2222', '03-4444-5555', '11204',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP029', '鈴木 三郎', 'スズキ サブロウ', 'password', '080-2222-3333', '03-6666-7777', '11204',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP030', '高橋 四郎', 'タカハシ シロウ', 'password', '090-3333-4444', '03-8888-9999', '11204',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP031', '山田 太郎', 'ヤマダ タロウ', 'password', '090-1234-5678', '03-1234-5678', '12101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP032', '鈴木 一郎', 'スズキ イチロウ', 'password', '090-2345-6789', '03-2345-6789', '12101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP033', '佐藤 花子', 'サトウ ハナコ', 'password', '090-3456-7890', '03-3456-7890', '12101',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP034', '山田太郎', 'やまだたろう', 'password', '090-1234-5678', '03-1234-5678', '12102',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP035', '山田 太郎', 'ヤマダ タロウ', 'password', '090-1234-5678', '03-1234-5678', '12203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP036', '佐藤 花子', 'サトウ ハナコ', 'password', '090-2345-6789', '03-2345-6789', '12203',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');
INSERT INTO public."社員マスタ" ("社員コード", "社員名", "社員名カナ", "パスワード", "電話番号", "FAX番号",
                                 "部門コード", "開始日", "職種コード", "承認権限コード", "作成日時", "作成者名",
                                 "更新日時", "更新者名")
VALUES ('EMP037', '田中 太郎', 'タナカ タロウ', 'password', '090-1234-5678', '03-1234-5678', '12204',
        '2021-01-01 00:00:00.000000', '', '', '2023-04-20 00:00:00.000000', 'admin', '2023-04-20 00:00:00.000000',
        'admin');

INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00101001', '牛肉', 0, '00100000~00101000~00101001', 1, '2022-04-20 00:00:00.000000', 'admin',
        '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00101002', '豚肉', 0, '00100000~00101000~00101002', 1, '2022-04-20 00:00:00.000000', 'admin',
        '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00102001', 'まぐろ', 0, '00100000~00102000~00101001', 1, '2022-04-20 00:00:00.000000', 'admin',
        '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00102002', 'えび', 0, '00100000~00102000~00101002', 1, '2022-04-20 00:00:00.000000', 'admin',
        '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00101000', '食肉', 1, '00100000~00101000', 0, '2022-04-20 00:00:00.000000', 'admin',
        '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00102000', '水産物', 1, '00100000~00102000', 0, '2022-04-20 00:00:00.000000', 'admin',
        '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品分類マスタ" ("商品分類コード", "商品分類名", "商品分類階層", "商品分類パス", "最下層区分",
                                     "作成日時", "作成者名", "更新日時", "更新者名")
VALUES ('00100000', '生鮮食品', 2, '00100000', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000',
        'admin');

INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10101001', '牛ひれ', '牛ひれ', '牛ひれ', '1', '1234567890', 1000, 900, 500, 1, '00101001', 1, 1, 1, '001', 0,
        '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10101002', '牛ロース', '牛ロース', '牛ロース', '1', '1234567890', 1000, 900, 500, 1, '00101001', 1, 1, 1,
        '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10102001', '豚ひれ', '豚ひれ', '豚ひれ', '1', '1234567890', 1000, 900, 500, 1, '00101002', 1, 1, 1, '001', 0,
        '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10102002', '豚ロース', '豚ロース', '豚ロース', '1', '1234567890', 1000, 900, 500, 1, '00101002', 1, 1, 1,
        '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10203001', 'まぐろトロ', 'まぐろトロ', 'まぐろトロ', '1', '1234567890', 1000, 900, 500, 1, '00102001', 1, 1, 1,
        '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10203002', 'まぐろ赤身', 'まぐろ赤身', 'まぐろ赤身', '1', '1234567890', 1000, 900, 500, 1, '00102001', 1, 1, 1,
        '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10204002', 'ブラックタイガー', 'ブラックタイガー', 'ブラックタイガー', '1', '1234567890', 1000, 900, 500, 1,
        '00102002', 1, 1, 1, '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('10204003', '大正えび', '大正えび', '大正えび', '1', '1234567890', 1000, 900, 500, 1, '00102002', 1, 1, 1,
        '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('001', 'いちご蒸缶', 'いちご蒸缶', 'いちご蒸缶', '2', '1234567890', 1000, 900, 500, 1, '00102002', 1, 1, 1,
        '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('002', 'いちご蒸缶セット', 'いちご蒸缶セット', 'いちご蒸缶セット', '2', '1234567890', 1000, 900, 500, 1,
        '00102002', 1, 1, 1, '001', 0, '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('X01', '生ウニ', '生ウニ', '生ウニ', '3', '1234567890', 1000, 900, 500, 1, '00102002', 1, 1, 1, '001', 0,
        '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('X02', '大アワビ', '大アワビ', '大アワビ', '3', '1234567890', 1000, 900, 500, 1, '00102002', 1, 1, 1, '001', 0,
        '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
INSERT INTO public."商品マスタ" ("商品コード", "商品正式名", "商品略称", "商品名カナ", "商品区分", "製品型番",
                                 "販売単価", "仕入単価", "売上原価", "税区分", "商品分類コード", "雑区分",
                                 "在庫管理対象区分", "在庫引当区分", "仕入先コード", "仕入先枝番", "作成日時",
                                 "作成者名", "更新日時", "更新者名")
VALUES ('Z01', '缶', '缶', '缶', '4', '1234567890', 1000, 900, 500, 1, '00102002', 1, 1, 1, '001', 0,
        '2022-04-20 00:00:00.000000', 'admin', '2022-04-20 00:00:00.000000', 'admin');
