ALTER TABLE org_contracts
ALTER COLUMN sub_name SET NOT NULL,
DROP CONSTRAINT IF EXISTS org_contracts_pkey,
ADD CONSTRAINT org_contracts_pkey PRIMARY KEY (org_code, product_code, sub_name);