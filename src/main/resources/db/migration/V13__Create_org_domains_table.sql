create table org_domains (id uuid not null, created_at timestamp default CURRENT_TIMESTAMP not null, created_by varchar(255), modified_at timestamp, modified_by varchar(255), domain varchar(255), organization_id uuid, primary key (id));
