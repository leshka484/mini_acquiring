UPDATE status.operation_status
SET code = 'REJECTED'
WHERE code = 'CANCELLED';

INSERT INTO status.operation_status (name, code)
VALUES ('Возвращена', 'REFUNDED');