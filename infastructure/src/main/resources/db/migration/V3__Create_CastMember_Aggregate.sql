
-- user: joseMarciano
CREATE TABLE CAST_MEMBERS (
    ID CHAR(32) NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    TYPE VARCHAR(100) NOT NULL,
    CREATED_AT DATETIME(6) NOT NULL,
    UPDATED_AT DATETIME(6) NOT NULL
);
-- rollback DROP CAST_MEMBERS