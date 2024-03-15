ALTER TABLE subscriptions
ADD COLUMN IF NOT EXISTS suspension_date date,
ADD COLUMN IF NOT EXISTS termination_date date,
ADD COLUMN IF NOT EXISTS deletion_date date;