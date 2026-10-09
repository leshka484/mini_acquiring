INSERT INTO "type".commission_type
(name, code)
VALUES
    ('Процентная', 'PERCENTAGE'),
    ('Фиксированная', 'FIXED');

INSERT INTO "type".operation_type
(name, code)
VALUES
    ('Оплата', 'PAYMENT'),
    ('Возврат', 'RETURN');

INSERT INTO status.merchant_status
(name, code)
VALUES
    ('Активный', 'ACTIVE'),
    ('Отключен', 'DISABLED');

INSERT INTO status.operation_status
(name, code)
VALUES
    ('Новая', 'NEW'),
    ('Оплачена', 'PAID'),
    ('Завершена', 'COMPLETED'),
    ('Отклонена', 'CANCELLED'),
    ('С ошибкой', 'FAILED');