ALTER TABLE acc_domains
ADD COLUMN IF NOT EXISTS dns_mx TEXT,
ADD COLUMN IF NOT EXISTS dns_spf TEXT,
ADD COLUMN IF NOT EXISTS dns_dkim TEXT,
ADD COLUMN IF NOT EXISTS dns_dmarc TEXT,
ADD COLUMN IF NOT EXISTS dns_dmarc_report TEXT,
ADD COLUMN IF NOT EXISTS dns_recommended TEXT,
DROP COLUMN IF EXISTS emailDnsRecords;
