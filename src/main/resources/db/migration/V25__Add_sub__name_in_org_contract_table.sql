ALTER TABLE org_contracts
ADD COLUMN IF NOT EXISTS sub_name character varying(255) pg_catalog."default" NOT NULL,
DROP CONSTRAINT IF EXISTS org_contracts_pkey,
ADD CONSTRAINT org_contracts_pkey PRIMARY KEY (org_code, product_code, sub_name);