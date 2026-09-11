CREATE SCHEMA IF NOT EXISTS commission;

CREATE  TABLE commission.commission_type IF NOT EXISTS (
	id                   bigint  NOT NULL  ,
	"type"               varchar(255)  NOT NULL  ,
	CONSTRAINT pk_commission_type PRIMARY KEY ( id )
 );

CREATE SCHEMA IF NOT EXISTS core;

CREATE  TABLE core.merchant IF NOT EXISTS ( -- Добавь отдельно таблицу статуса мерчанта (merchant_status)
	id                   bigint  NOT NULL  ,
	commission_value     numeric(38,2)  NOT NULL  ,
	name                 text  NOT NULL  ,
	commission_type      bigint  NOT NULL  ,
	CONSTRAINT pk_merchant PRIMARY KEY ( id )
 );

CREATE  TABLE core."operation" IF NOT EXISTS (
	id                   bigint  NOT NULL  ,
	created_at           timestamp  NOT NULL  ,
	parent_id            bigint    ,
	processed_at         timestamp    ,
	"sum"                numeric(38,2)  NOT NULL  ,
	merchant_id          bigint  NOT NULL  ,
	status_id            bigint  NOT NULL  ,
	operation_type_id    bigint  NOT NULL  ,
	CONSTRAINT pk_operati PRIMARY KEY ( id )
 );

CREATE INDEX idx_operation_merchant_id ON core."operation"  ( merchant_id );

CREATE INDEX idx_operation_status_id ON core."operation"  ( status_id );

CREATE  TABLE core.commission IF NOT EXISTS (
	id                   bigint  NOT NULL  ,
	processed_at         timestamp  NOT NULL  ,
	total_commission     numeric(38,2)  NOT NULL  ,
	operation_id         bigint  NOT NULL  ,
	CONSTRAINT pk_commission PRIMARY KEY ( id )
 );

CREATE INDEX idx_commission_operation_id ON core.commission  ( operation_id );

ALTER TABLE core.commission ADD CONSTRAINT fk_commission_merchant FOREIGN KEY ( operation_id ) REFERENCES core.merchant( id );

ALTER TABLE core.merchant ADD CONSTRAINT fk_merchant_commission_type FOREIGN KEY ( commission_type ) REFERENCES commission.commission_type( id );

ALTER TABLE core."operation" ADD CONSTRAINT fk_operation_merchant FOREIGN KEY ( merchant_id ) REFERENCES core.merchant( id );

CREATE SCHEMA IF NOT EXISTS "operation"; --Раздели схемы следующим образом: core, status, type

CREATE  TABLE "operation".operation_status IF NOT EXISTS (
	id                   bigint  NOT NULL  ,
	status               varchar(255)  NOT NULL  ,
	CONSTRAINT pk_operation_status PRIMARY KEY ( id )
 );

CREATE  TABLE "operation".operation_type IF NOT EXISTS (
	id                   bigint  NOT NULL  ,
	"type"               varchar(255)  NOT NULL  ,
	CONSTRAINT pk_operation_type PRIMARY KEY ( id )
 );

ALTER TABLE core."operation" ADD CONSTRAINT fk_operation_operation_status FOREIGN KEY ( status_id ) REFERENCES "operation".operation_status( id );

ALTER TABLE core."operation" ADD CONSTRAINT fk_operation_operation_type FOREIGN KEY ( operation_type_id ) REFERENCES "operation".operation_type( id );

