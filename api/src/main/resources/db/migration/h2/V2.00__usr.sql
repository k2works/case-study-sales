-- スキーマを作成
CREATE SCHEMA system;

-- テーブルを作成
CREATE TABLE system.usr
(
    user_id    VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
);

-- ダミーユーザー(password = demo)
INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('taro-yamada', '太郎', '山田', '$2a$10$oxSJ1.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 'USER');

-- 認証確認用のテストユーザー(password = demo)
INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('aaaa', 'Aaa', 'Aaa', '$2a$10$oxSJ1.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 'USER');
INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('bbbb', 'Bbb', 'Bbb', '$2a$10$oxSJ1.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 'USER');
INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('cccc', 'Ccc', 'Ccc', '$2a$10$oxSJ1.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 'ADMIN');
