CREATE TABLE IF NOT EXISTS user_seq (next_val BIGINT) ENGINE=InnoDB;
INSERT INTO user_seq VALUES (1);

CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL,
    full_name VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
