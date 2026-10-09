ALTER TABLE core."operation"
ADD public_id uuid DEFAULT
gen_random_uuid() NOT NULL;

ALTER TABLE core.merchant
ADD public_id uuid DEFAULT
gen_random_uuid() NOT NULL;