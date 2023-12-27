ALTER TABLE users ADD COLUMN IF NOT EXISTS contact_id uuid,
ADD CONSTRAINT fk9s0tlg428ntp5edfquo0r9jxs FOREIGN KEY (contact_id)
REFERENCES public.contacts (id);