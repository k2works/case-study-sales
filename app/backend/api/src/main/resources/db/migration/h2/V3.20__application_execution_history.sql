DROP TABLE IF EXISTS system.application_execution_history;

CREATE TABLE system.application_execution_history (
                                                      id SERIAL PRIMARY KEY,
                                                      process_name VARCHAR(255) NOT NULL,
                                                      process_code VARCHAR(40) NOT NULL,
                                                      process_type VARCHAR(10) NOT NULL,
                                                      process_start TIMESTAMP NOT NULL,
                                                      process_end TIMESTAMP,
                                                      process_flag INTEGER DEFAULT 0,
                                                      process_details TEXT,
                                                      version INTEGER DEFAULT 0,
                                                      user_id varchar(255) references system.usr(user_id) on delete set null
);

COMMENT ON TABLE system.application_execution_history IS 'アプリケーション実行履歴';
COMMENT ON COLUMN system.application_execution_history.process_name IS 'プロセス名';
COMMENT ON COLUMN system.application_execution_history.process_code IS 'プロセスコード';
COMMENT ON COLUMN system.application_execution_history.process_type IS 'プロセスタイプ';
COMMENT ON COLUMN system.application_execution_history.process_start IS 'プロセス開始日時';
COMMENT ON COLUMN system.application_execution_history.process_end IS 'プロセス終了日時';
COMMENT ON COLUMN system.application_execution_history.process_flag IS 'プロセスフラグ';
COMMENT ON COLUMN system.application_execution_history.process_details IS 'プロセス詳細';