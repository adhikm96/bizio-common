ALTER TABLE acc_domains RENAME COLUMN mx_record To email_dns_records;
ALTER TABLE acc_domains RENAME COLUMN txt_record To domain_dns_record;