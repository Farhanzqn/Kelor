CREATE SEQUENCE "DOMAIN_EVENT_ENTRY_SEQ" START WITH 1 INCREMENT 50;
CREATE SEQUENCE "ASSOCIATION_VALUE_ENTRY_SEQ" START WITH 1 INCREMENT 50;

CREATE TABLE "ASSOCIATION_VALUE_ENTRY"
(
    "ID"                BIGINT       NOT NULL,
    "ASSOCIATION_KEY"   VARCHAR(255) NOT NULL,
    "ASSOCIATION_VALUE" VARCHAR(255),
    "SAGA_ID"           VARCHAR(255) NOT NULL,
    "SAGA_TYPE"         VARCHAR(255)
);
ALTER TABLE "ASSOCIATION_VALUE_ENTRY"
    ADD CONSTRAINT "CONSTRAINT_B" PRIMARY KEY ("ID");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.ASSOCIATION_VALUE_ENTRY;
CREATE INDEX "IDXK45EQNXKGD8HPDN6XIXN8SGFT" ON "ASSOCIATION_VALUE_ENTRY" ("SAGA_TYPE", "ASSOCIATION_KEY", "ASSOCIATION_VALUE");
CREATE INDEX "IDXGV5K1V2MH6FRXUY5C0HGBAU94" ON "ASSOCIATION_VALUE_ENTRY" ("SAGA_ID", "SAGA_TYPE");
CREATE TABLE "CMD_COURIER_ENTITY"
(
    "ID"                          VARCHAR(255) NOT NULL,
    "MAX_NUMBER_OF_ACTIVE_ORDERS" INTEGER,
    "NUMBER_OF_ACTIVE_ORDERS"     INTEGER
);
ALTER TABLE "CMD_COURIER_ENTITY"
    ADD CONSTRAINT "CONSTRAINT_F" PRIMARY KEY ("ID");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.CMD_COURIER_ENTITY;
CREATE TABLE "COURIER_ENTITY"
(
    "ID"                          VARCHAR(255) NOT NULL,
    "FIRST_NAME"                  VARCHAR(255),
    "LAST_NAME"                   VARCHAR(255),
    "MAX_NUMBER_OF_ACTIVE_ORDERS" INTEGER,
    "NUMBER_OF_ACTIVE_ORDERS"     INTEGER
);
ALTER TABLE "COURIER_ENTITY"
    ADD CONSTRAINT "CONSTRAINT_F7" PRIMARY KEY ("ID");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.COURIER_ENTITY;
CREATE TABLE "SAGA_ENTRY"
(
    "SAGA_ID"         VARCHAR(255) NOT NULL,
    "REVISION"        VARCHAR(255),
    "SAGA_TYPE"       VARCHAR(255),
    "SERIALIZED_SAGA" BLOB
);
ALTER TABLE "SAGA_ENTRY"
    ADD CONSTRAINT "CONSTRAINT_BD" PRIMARY KEY ("SAGA_ID");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.SAGA_ENTRY;
CREATE TABLE "SHIPMENT_ENTITY"
(
    "ID"         VARCHAR(255) NOT NULL,
    "ADDRESS"    VARCHAR(255),
    "COURIER_ID" VARCHAR(255),
    "STATE"      INTEGER
);
ALTER TABLE "SHIPMENT_ENTITY"
    ADD CONSTRAINT "CONSTRAINT_1" PRIMARY KEY ("ID");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.SHIPMENT_ENTITY;
CREATE TABLE "TOKEN_ENTRY"
(
    "PROCESSOR_NAME" VARCHAR(255) NOT NULL,
    "SEGMENT"        INTEGER      NOT NULL,
    "OWNER"          VARCHAR(255),
    "TIMESTAMP"      VARCHAR(255) NOT NULL,
    "TOKEN"          BLOB,
    "TOKEN_TYPE"     VARCHAR(255)
);
ALTER TABLE "TOKEN_ENTRY"
    ADD CONSTRAINT "CONSTRAINT_7" PRIMARY KEY ("PROCESSOR_NAME", "SEGMENT");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.TOKEN_ENTRY;

CREATE
MEMORY
TABLE "DEAD_LETTER_ENTRY"
(
    "DEAD_LETTER_ID" CHARACTER VARYING(255) NOT NULL,
    "CAUSE_MESSAGE" CHARACTER VARYING(255),
    "CAUSE_TYPE" CHARACTER VARYING(255),
    "DIAGNOSTICS" BINARY LARGE OBJECT,
    "ENQUEUED_AT" TIMESTAMP NOT NULL,
    "LAST_TOUCHED" TIMESTAMP,
    "AGGREGATE_IDENTIFIER" CHARACTER VARYING(255),
    "EVENT_IDENTIFIER" CHARACTER VARYING(255) NOT NULL,
    "MESSAGE_TYPE" CHARACTER VARYING(255) NOT NULL,
    "META_DATA" BINARY LARGE OBJECT,
    "PAYLOAD" BINARY LARGE OBJECT NOT NULL,
    "PAYLOAD_REVISION" CHARACTER VARYING(255),
    "PAYLOAD_TYPE" CHARACTER VARYING(255) NOT NULL,
    "SEQUENCE_NUMBER" BIGINT,
    "TIME_STAMP" CHARACTER VARYING(255) NOT NULL,
    "TOKEN" BINARY LARGE OBJECT,
    "TOKEN_TYPE" CHARACTER VARYING(255),
    "TYPE" CHARACTER VARYING(255),
    "PROCESSING_GROUP" CHARACTER VARYING(255) NOT NULL,
    "PROCESSING_STARTED" TIMESTAMP,
    "SEQUENCE_IDENTIFIER" CHARACTER VARYING(255) NOT NULL,
    "SEQUENCE_INDEX" BIGINT NOT NULL
);
ALTER TABLE "DEAD_LETTER_ENTRY"
    ADD CONSTRAINT "CONSTRAINT_5" PRIMARY KEY ("DEAD_LETTER_ID");
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.DEAD_LETTER_ENTRY;
CREATE INDEX "IDXE67WCX5FIQ9HL4Y4QKHLCJ9CG" ON "DEAD_LETTER_ENTRY" ("PROCESSING_GROUP" NULLS FIRST);
CREATE INDEX "IDXRWUCPGS6SN93LDGOEH2Q9K6BN" ON "DEAD_LETTER_ENTRY" ("PROCESSING_GROUP" NULLS FIRST, "SEQUENCE_IDENTIFIER" NULLS FIRST);


CREATE
MEMORY
TABLE "DOMAIN_EVENT_ENTRY"
(
    "GLOBAL_INDEX" BIGINT NOT NULL,
    "EVENT_IDENTIFIER" CHARACTER VARYING(255) NOT NULL,
    "META_DATA" BINARY LARGE OBJECT,
    "PAYLOAD" BINARY LARGE OBJECT NOT NULL,
    "PAYLOAD_REVISION" CHARACTER VARYING(255),
    "PAYLOAD_TYPE" CHARACTER VARYING(255) NOT NULL,
    "TIME_STAMP" CHARACTER VARYING(255) NOT NULL,
    "AGGREGATE_IDENTIFIER" CHARACTER VARYING(255) NOT NULL,
    "SEQUENCE_NUMBER" BIGINT NOT NULL,
    "TYPE" CHARACTER VARYING(255)
);
ALTER TABLE "DOMAIN_EVENT_ENTRY"
    ADD CONSTRAINT "CONSTRAINT_8" PRIMARY KEY ("GLOBAL_INDEX");


CREATE
MEMORY
TABLE "SNAPSHOT_EVENT_ENTRY"
(
    "AGGREGATE_IDENTIFIER" CHARACTER VARYING(255) NOT NULL,
    "SEQUENCE_NUMBER" BIGINT NOT NULL,
    "TYPE" CHARACTER VARYING(255) NOT NULL,
    "EVENT_IDENTIFIER" CHARACTER VARYING(255) NOT NULL,
    "META_DATA" BINARY LARGE OBJECT,
    "PAYLOAD" BINARY LARGE OBJECT NOT NULL,
    "PAYLOAD_REVISION" CHARACTER VARYING(255),
    "PAYLOAD_TYPE" CHARACTER VARYING(255) NOT NULL,
    "TIME_STAMP" CHARACTER VARYING(255) NOT NULL
);
ALTER TABLE "SNAPSHOT_EVENT_ENTRY"
    ADD CONSTRAINT "CONSTRAINT_E" PRIMARY KEY ("AGGREGATE_IDENTIFIER", "SEQUENCE_NUMBER", "TYPE");
