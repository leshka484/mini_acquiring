INSERT INTO core.merchant
    (name, commission_value, commission_type, status)
VALUES
(
    'Percentage merchant',
    10,
    (
        SELECT id
        FROM "type".commission_type
        WHERE code = 'PERCENTAGE'
    ),
    (
        SELECT id
        FROM status.merchant_status
        WHERE code = 'ACTIVE'
    )
),
(
    'Fixed merchant',
    15,
    (
        SELECT id
        FROM "type".commission_type
        WHERE code = 'FIXED'
    ),
    (
        SELECT id
        FROM status.merchant_status
        WHERE code = 'ACTIVE'
    )
),
(
    'Disabled merchant',
    10,
    (
        SELECT id
        FROM "type".commission_type
        WHERE code = 'PERCENTAGE'
    ),
    (
        SELECT id
        FROM status.merchant_status
        WHERE code = 'DISABLED'
    )
);

INSERT INTO core."operation"
    (
        merchant_id,
        status_id,
        operation_type_id,
        "sum",
        created_at,
        processed_at,
        parent_id
    )
VALUES
(
    (
        SELECT id
        FROM core.merchant
        WHERE name = 'Percentage merchant'
    ),
    (
        SELECT id
        FROM status.operation_status
        WHERE code = 'NEW'
    ),
    (
        SELECT id
        FROM "type".operation_type
        WHERE code = 'PAYMENT'
    ),
    100000,
    '2026-01-01 10:30:00',
    NULL,
    NULL
),
(
    (
        SELECT id
        FROM core.merchant
        WHERE name = 'Fixed merchant'
    ),
    (
        SELECT id
        FROM status.operation_status
        WHERE code = 'NEW'
    ),
    (
        SELECT id
        FROM "type".operation_type
        WHERE code = 'PAYMENT'
    ),
    100000,
    '2026-01-01 10:30:00',
    NULL,
    NULL
),
(
    (
        SELECT id
        FROM core.merchant
        WHERE name = 'Percentage merchant'
    ),
    (
        SELECT id
        FROM status.operation_status
        WHERE code = 'PAID'
    ),
    (
        SELECT id
        FROM "type".operation_type
        WHERE code = 'PAYMENT'
    ),
    200000,
    '2026-01-01 10:30:00',
    NULL,
    NULL
),
(
    (
        SELECT id
        FROM core.merchant
        WHERE name = 'Percentage merchant'
    ),
    (
        SELECT id
        FROM status.operation_status
        WHERE code = 'COMPLETED'
    ),
    (
        SELECT id
        FROM "type".operation_type
        WHERE code = 'PAYMENT'
    ),
    100000,
    '2026-01-01 10:30:00',
    NULL,
    NULL
),
(
    (
        SELECT id
        FROM core.merchant
        WHERE name = 'Percentage merchant'
    ),
    (
        SELECT id
        FROM status.operation_status
        WHERE code = 'CANCELLED'
    ),
    (
        SELECT id
        FROM "type".operation_type
        WHERE code = 'PAYMENT'
    ),
    100000,
    '2026-01-01 10:30:00',
    NULL,
    NULL
),
(
    (
        SELECT id
        FROM core.merchant
        WHERE name = 'Percentage merchant'
    ),
    (
        SELECT id
        FROM status.operation_status
        WHERE code = 'FAILED'
    ),
    (
        SELECT id
        FROM "type".operation_type
        WHERE code = 'PAYMENT'
    ),
    100000,
    '2026-01-01 10:30:00',
    NULL,
    NULL
);
