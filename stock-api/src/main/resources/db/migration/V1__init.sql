CREATE TABLE day
(
    id   BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL UNIQUE
);

CREATE TABLE share
(
    id                      BIGSERIAL PRIMARY KEY,
    day                     BIGINT       NOT NULL REFERENCES day,
    ticker                  VARCHAR(5)   NOT NULL,
    name                    VARCHAR(128),
    isin                    VARCHAR(12),
    currency                VARCHAR(3),
    marketplace             VARCHAR(3),
    list                    VARCHAR(128),
    average_price           DECIMAL(32, 10),
    open_price              DECIMAL(32, 10),
    high_price              DECIMAL(32, 10),
    low_price               DECIMAL(32, 10),
    last_close_price        DECIMAL(32, 10),
    last_price              DECIMAL(32, 10),
    price_change_percentage DECIMAL(32, 10),
    best_bid                DECIMAL(32, 10),
    best_ask                DECIMAL(32, 10),
    trades                  INTEGER,
    volume                  BIGINT,
    turnover                DECIMAL(32, 10),
    industry                VARCHAR(128) NOT NULL,
    supersector             VARCHAR(128) NOT NULL,
    UNIQUE (day, ticker)
);