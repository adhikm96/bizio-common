ALTER TABLE acc_domains ADD COLUMN IF NOT EXISTS org_id uuid;
alter table acc_domains add constraint fk5ibpf85d7mk1h07ge6ntcc4ie foreign key (org_id) references organizations;