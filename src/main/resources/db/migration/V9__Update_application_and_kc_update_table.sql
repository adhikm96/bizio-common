CREATE TABLE IF NOT EXISTS public.application_default_roles
(
    application_id uuid NOT NULL,
    role_id uuid NOT NULL
);

ALTER TABLE public.application_default_roles
    DROP CONSTRAINT IF EXISTS fkg7h0dpf1eayxy5ixmfhtq3fk3,
    ADD CONSTRAINT fkg7h0dpf1eayxy5ixmfhtq3fk3 FOREIGN KEY (application_id)
        REFERENCES public.applications (id),
    DROP CONSTRAINT IF EXISTS fkiuiisag1bjtlgih2kvlwnlkt,
    ADD CONSTRAINT fkiuiisag1bjtlgih2kvlwnlkt FOREIGN KEY (role_id)
        REFERENCES public.roles (id);

ALTER TABLE public.kc_updates ADD update_type integer DEFAULT 0;