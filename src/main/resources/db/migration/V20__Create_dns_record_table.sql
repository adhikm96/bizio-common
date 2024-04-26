create table dns_records (id uuid not null, created_at timestamp default CURRENT_TIMESTAMP not null, created_by varchar(255), modified_at timestamp, modified_by varchar(255),
type integer, name varchar(255), ttl integer, value varchar(255), acc_domain_id uuid, primary key (id));

alter table dns_records add constraint fkvqjmhpnaknhkanebmv7br831 foreign key (acc_domain_id) references acc_domains;

ALTER TABLE acc_domains
DROP COLUMN IF EXISTS mx_record,
DROP COLUMN IF EXISTS txt_record;