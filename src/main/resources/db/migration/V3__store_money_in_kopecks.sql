ALTER TABLE core.commission
ALTER COLUMN total_commission
TYPE bigint;

ALTER TABLE core."operation"
ALTER COLUMN "sum"
TYPE bigint;