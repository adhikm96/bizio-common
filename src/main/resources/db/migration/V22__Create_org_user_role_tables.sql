CREATE TABLE IF NOT EXISTS public.org_user_roles
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    org_id uuid,
    user_id uuid,
    role_id uuid,
    CONSTRAINT org_user_roles_pkey PRIMARY KEY (id)
);

ALTER TABLE public.org_user_roles
    ADD CONSTRAINT fka7bp60nyyv524m8klpvptvs1g FOREIGN KEY (user_id)
        REFERENCES public.users (id),
    ADD CONSTRAINT fkayp0u71n9j3vdtqtugif6579c FOREIGN KEY (role_id)
        REFERENCES public.roles (id),
    ADD CONSTRAINT fks9gdklsl0s4nl6u36m60f7ly FOREIGN KEY (org_id)
        REFERENCES public.organizations (id);