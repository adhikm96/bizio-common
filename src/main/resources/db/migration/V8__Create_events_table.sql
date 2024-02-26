create table events (event_key varchar(255) not null, code varchar(255), encrypted_key varchar(255), encrypted_key_name varchar(255), name varchar(255), module_id uuid, primary key (event_key));
alter table if exists events add constraint FKhxbsxmxmjarskyd808fqem44c foreign key (module_id) references modules;
