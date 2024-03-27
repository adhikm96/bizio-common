DROP TABLE IF EXISTS org_domains;

ALTER TABLE leads DROP COLUMN IF EXISTS emailDomain;

ALTER TABLE settings DROP COLUMN IF EXISTS whitelisted_numbers,
ADD COLUMN IF NOT EXISTS personal_threshold integer,
ADD COLUMN IF NOT EXISTS business_threshold integer;

create table acc_domains (id uuid not null, created_at timestamp default CURRENT_TIMESTAMP not null, created_by varchar(255), modified_at timestamp, modified_by varchar(255), status integer, domain varchar(255), account_id uuid, primary key (id));
alter table acc_domains add constraint fkgksrwyx6t3dhg4ceb0rul0dlc foreign key (account_id) references accounts;