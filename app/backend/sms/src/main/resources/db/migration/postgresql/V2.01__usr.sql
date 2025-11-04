DELETE
FROM system.usr;

INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('U000001', 'Aaa', 'Aaa', '$2a$10$9I4C0./pQ/Lt8WqCB/4Tfu1njw3m5ZK3.x6kxo1Ilwp.MQr1Hx82i', 'USER');
INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('U000002', 'Bbb', 'Bbb', '$2a$10$9I4C0./pQ/Lt8WqCB/4Tfu1njw3m5ZK3.x6kxo1Ilwp.MQr1Hx82i', 'USER');
INSERT INTO system.usr (user_id, first_name, last_name, password, role_name)
VALUES ('U000003', 'Ccc', 'Ccc', '$2a$10$9I4C0./pQ/Lt8WqCB/4Tfu1njw3m5ZK3.x6kxo1Ilwp.MQr1Hx82i', 'ADMIN');
