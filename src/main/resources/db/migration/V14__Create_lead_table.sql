CREATE TABLE IF NOT EXISTS public.leads
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    acc_type integer,
    address_line1 character varying(255) COLLATE pg_catalog."default",
    address_line2 character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    country character varying(255) COLLATE pg_catalog."default",
    dob date,
    first_name character varying(255) COLLATE pg_catalog."default",
    gender integer,
    job_title character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    mobile character varying(255) COLLATE pg_catalog."default",
    org_name character varying(255) COLLATE pg_catalog."default",
    signup_email character varying(255) COLLATE pg_catalog."default",
    state character varying(255) COLLATE pg_catalog."default",
    status integer,
    stay_informed_about_bizio boolean DEFAULT false,
    terms_conditions_agreed boolean DEFAULT false,
    tax_id character varying(255) COLLATE pg_catalog."default",
    type_of_business character varying(255) COLLATE pg_catalog."default",
    website character varying(255) COLLATE pg_catalog."default",
    email_domain character varying(255) COLLATE pg_catalog."default",
    work_email character varying(255) COLLATE pg_catalog."default",
    phone_number character varying(255) COLLATE pg_catalog."default",
    zipcode character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT leads_pkey PRIMARY KEY (id)
);

ALTER TABLE orders ADD COLUMN IF NOT EXISTS lead_id uuid,
ADD CONSTRAINT fkginbhj3fpnxvxfiybfkw7hmpv FOREIGN KEY (lead_id)
REFERENCES public.leads (id);

ALTER TABLE accounts
ADD COLUMN IF NOT EXISTS signup_email VARCHAR(255),
ADD COLUMN IF NOT EXISTS email VARCHAR(255),
ADD COLUMN IF NOT EXISTS phone VARCHAR(255);

ALTER TABLE users
DROP COLUMN IF EXISTS designation,
ADD COLUMN IF NOT EXISTS job_title VARCHAR(255);

ALTER TABLE settings
ADD COLUMN IF NOT EXISTS whitelisted_numbers TEXT;
