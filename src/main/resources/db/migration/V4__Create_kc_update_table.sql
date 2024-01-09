CREATE TABLE IF NOT EXISTS public.kc_updates
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    status integer,
    user_id uuid,
    CONSTRAINT kc_updates_pkey PRIMARY KEY (id),
    CONSTRAINT fkb0pe8lwcxgfaxc9sjf0m3mas2 FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);