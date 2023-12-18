CREATE TABLE IF NOT EXISTS public.admin_users
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    avatar character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    country character varying(255) COLLATE pg_catalog."default",
    dob date,
    email character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    gender integer,
    last_name character varying(255) COLLATE pg_catalog."default",
    middle_name character varying(255) COLLATE pg_catalog."default",
    mobile character varying(255) COLLATE pg_catalog."default",
    oidc_id uuid,
    state character varying(255) COLLATE pg_catalog."default",
    status integer,
    street character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    zip_code character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT admin_users_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.accounts
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(64) COLLATE pg_catalog."default" NOT NULL,
    status integer,
    type integer,
    primary_contact_id uuid,
    primary_organization_id uuid,
    CONSTRAINT accounts_pkey PRIMARY KEY (id),
    CONSTRAINT uk_p2jd4db8821l8voctujboa9oh UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS public.actions
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT actions_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.addresses
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    address_line1 character varying(255) COLLATE pg_catalog."default",
    address_line2 character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    country character varying(255) COLLATE pg_catalog."default",
    primary_address boolean DEFAULT false,
    state character varying(255) COLLATE pg_catalog."default",
    status integer,
    zipcode character varying(255) COLLATE pg_catalog."default",
    acc_id uuid,
    org_id uuid,
    user_id uuid,
    CONSTRAINT addresses_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.application_primary_user_roles
(
    application_id uuid NOT NULL,
    role_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.application_resources
(
    application_id uuid NOT NULL,
    resource_id uuid NOT NULL
);


CREATE TABLE IF NOT EXISTS public.applications
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    app_url character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    icon_url character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    project_id uuid,
    CONSTRAINT applications_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.attributes
(
    code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    attribute_type integer,
    description character varying(255) COLLATE pg_catalog."default",
    merge_type integer DEFAULT 0,
    status integer,
    CONSTRAINT attributes_pkey PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS public.batch_job_execution
(
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10) COLLATE pg_catalog."default",
    exit_code character varying(2500) COLLATE pg_catalog."default",
    exit_message character varying(2500) COLLATE pg_catalog."default",
    last_updated timestamp without time zone,
    job_configuration_location character varying(2500) COLLATE pg_catalog."default",
    CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id)
);

CREATE TABLE IF NOT EXISTS public.batch_job_execution_context
(
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) COLLATE pg_catalog."default" NOT NULL,
    serialized_context text COLLATE pg_catalog."default",
    CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id)
);

CREATE TABLE IF NOT EXISTS public.batch_job_execution_params
(
    job_execution_id bigint NOT NULL,
    type_cd character varying(6) COLLATE pg_catalog."default" NOT NULL,
    key_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    string_val character varying(250) COLLATE pg_catalog."default",
    date_val timestamp without time zone,
    long_val bigint,
    double_val double precision,
    identifying character(1) COLLATE pg_catalog."default" NOT NULL
);

CREATE TABLE IF NOT EXISTS public.batch_job_instance
(
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    job_key character varying(32) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id),
    CONSTRAINT job_inst_un UNIQUE (job_name, job_key)
);

CREATE TABLE IF NOT EXISTS public.batch_step_execution
(
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    job_execution_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status character varying(10) COLLATE pg_catalog."default",
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(2500) COLLATE pg_catalog."default",
    exit_message character varying(2500) COLLATE pg_catalog."default",
    last_updated timestamp without time zone,
    CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id)
);

CREATE TABLE IF NOT EXISTS public.batch_step_execution_context
(
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) COLLATE pg_catalog."default" NOT NULL,
    serialized_context text COLLATE pg_catalog."default",
    CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id)
);

CREATE TABLE IF NOT EXISTS public.billing_accounts
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    acc_holder_name character varying(255) COLLATE pg_catalog."default",
    acc_holder_type character varying(255) COLLATE pg_catalog."default",
    bank_name character varying(255) COLLATE pg_catalog."default",
    bank_routing_number character varying(255) COLLATE pg_catalog."default",
    billing_acc_type integer,
    card_brand character varying(255) COLLATE pg_catalog."default",
    card_type integer,
    exp_month character varying(255) COLLATE pg_catalog."default",
    exp_year character varying(255) COLLATE pg_catalog."default",
    financial_connections_acc character varying(255) COLLATE pg_catalog."default",
    fingerprint character varying(255) COLLATE pg_catalog."default",
    last4 character varying(255) COLLATE pg_catalog."default",
    pg_acc_type character varying(255) COLLATE pg_catalog."default",
    primary_account boolean DEFAULT false,
    status integer,
    stripe_payment_method_id character varying(255) COLLATE pg_catalog."default",
    address_id uuid,
    organization_id uuid,
    CONSTRAINT billing_accounts_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.bundle_items
(
    id uuid NOT NULL,
    price_allocation double precision,
    bundle_item_id uuid,
    product_variant_id uuid,
    CONSTRAINT bundle_items_pkey PRIMARY KEY (id),
    CONSTRAINT uks7gj6s5795c8clmw856mxe7q4 UNIQUE (product_variant_id, bundle_item_id)
);

CREATE TABLE IF NOT EXISTS public.contact_us
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    company character varying(255) COLLATE pg_catalog."default",
    country character varying(255) COLLATE pg_catalog."default" NOT NULL,
    employees character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    job_title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    phone character varying(255) COLLATE pg_catalog."default" NOT NULL,
    product_interest character varying(255) COLLATE pg_catalog."default" NOT NULL,
    questions_comments character varying(255) COLLATE pg_catalog."default" NOT NULL,
    work_email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT contact_us_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.contacts
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    fax character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    middle_name character varying(255) COLLATE pg_catalog."default",
    mobile character varying(255) COLLATE pg_catalog."default",
    primary_contact boolean DEFAULT false,
    status integer,
    website character varying(255) COLLATE pg_catalog."default",
    acc_id uuid,
    org_id uuid,
    user_id uuid,
    CONSTRAINT contacts_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.contract_change_requests
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    entity_id character varying(255) COLLATE pg_catalog."default",
    entity_name character varying(255) COLLATE pg_catalog."default",
    new_record text COLLATE pg_catalog."default",
    old_record text COLLATE pg_catalog."default",
    payload text COLLATE pg_catalog."default",
    payload_action character varying(255) COLLATE pg_catalog."default",
    status integer,
    contract_migration_id uuid,
    CONSTRAINT contract_change_requests_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.contract_migrations
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT contract_migrations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.coupons
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    end_date timestamp without time zone,
    max_redemptions integer,
    name character varying(255) COLLATE pg_catalog."default",
    start_date timestamp without time zone,
    status integer,
    type integer,
    value real,
    CONSTRAINT coupons_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.invoices
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    discount numeric(19,2),
    discount_str text COLLATE pg_catalog."default",
    due_date date,
    gross_total numeric(19,2),
    net_total numeric(19,2),
    next_payment_attempt_date date,
    posting_date date,
    promo_code character varying(255) COLLATE pg_catalog."default",
    ref_no character varying(64) COLLATE pg_catalog."default" NOT NULL,
    status integer,
    tax numeric(19,2),
    tax_str text COLLATE pg_catalog."default",
    address_id uuid,
    payment_id uuid,
    price_id uuid,
    product_id uuid,
    product_variant_id uuid,
    subscription_id uuid,
    CONSTRAINT invoices_pkey PRIMARY KEY (id),
    CONSTRAINT uk_si44edgqgjx4difdvyw4dqhqk UNIQUE (ref_no)
);

CREATE TABLE IF NOT EXISTS public.modules
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status integer,
    project_id uuid NOT NULL,
    CONSTRAINT modules_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.order_payloads
(
    id uuid NOT NULL,
    payload text COLLATE pg_catalog."default",
    payload_type character varying(255) COLLATE pg_catalog."default",
    stripe_customer_id character varying(255) COLLATE pg_catalog."default",
    order_id uuid,
    CONSTRAINT order_payloads_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.orders
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    discount numeric(19,2),
    discount_str text COLLATE pg_catalog."default",
    gross_total numeric(19,2),
    net_total numeric(19,2),
    posting_date date,
    promo_code character varying(255) COLLATE pg_catalog."default",
    ref_no character varying(64) COLLATE pg_catalog."default" NOT NULL,
    status integer,
    tax numeric(19,2),
    tax_str text COLLATE pg_catalog."default",
    address_id uuid,
    organization_id uuid,
    parent_organization_id uuid,
    price_id uuid,
    product_id uuid,
    product_variant_id uuid,
    promotion_id uuid,
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT uk_jfkscgdl88ak8sjcrjb2qipov UNIQUE (ref_no)
);

CREATE TABLE IF NOT EXISTS public.org_contracts
(
    org_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    product_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    attributes text COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT org_contracts_pkey PRIMARY KEY (org_code, product_code)
);

CREATE TABLE IF NOT EXISTS public.org_join_requests
(
    id uuid NOT NULL,
    city character varying(255) COLLATE pg_catalog."default",
    country character varying(255) COLLATE pg_catalog."default",
    dob date,
    email character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    middle_name character varying(255) COLLATE pg_catalog."default",
    requested_email_domain character varying(255) COLLATE pg_catalog."default",
    state character varying(255) COLLATE pg_catalog."default",
    status integer,
    org_id uuid,
    CONSTRAINT org_join_requests_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.organization_users
(
    organization_id uuid NOT NULL,
    user_id uuid NOT NULL
);


CREATE TABLE IF NOT EXISTS public.organizations
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    class_name character varying(255) COLLATE pg_catalog."default",
    code character varying(64) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    email_domain character varying(255) COLLATE pg_catalog."default",
    exchange character varying(255) COLLATE pg_catalog."default",
    industry character varying(255) COLLATE pg_catalog."default",
    industry_type character varying(255) COLLATE pg_catalog."default",
    market character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    other_name character varying(255) COLLATE pg_catalog."default",
    short_name character varying(255) COLLATE pg_catalog."default",
    status integer,
    stripe_customer_id character varying(255) COLLATE pg_catalog."default",
    structure character varying(255) COLLATE pg_catalog."default",
    symbol character varying(255) COLLATE pg_catalog."default",
    account_id uuid,
    parent_id uuid,
    CONSTRAINT organizations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.payments
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    amount numeric(19,2),
    payment_date date,
    payment_ref character varying(255) COLLATE pg_catalog."default",
    ref_no character varying(64) COLLATE pg_catalog."default" NOT NULL,
    status integer,
    billing_account_id uuid,
    CONSTRAINT payments_pkey PRIMARY KEY (id),
    CONSTRAINT uk_aqqw86hshfouowhq9m29i6nro UNIQUE (ref_no)
);

CREATE TABLE IF NOT EXISTS public.policies
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    change_requested boolean DEFAULT false,
    code character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    project_id uuid,
    CONSTRAINT policies_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.prices
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    associated_function character varying(255) COLLATE pg_catalog."default",
    is_default boolean,
    name character varying(255) COLLATE pg_catalog."default",
    price double precision,
    status integer,
    product_variant_id uuid,
    CONSTRAINT prices_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.product_groups
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    pg_type integer NOT NULL DEFAULT 2,
    status integer,
    CONSTRAINT product_groups_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.product_upgrade_orders
(
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    "number" integer,
    product_id uuid NOT NULL,
    pg_id uuid NOT NULL,
    CONSTRAINT product_upgrade_orders_pkey PRIMARY KEY (pg_id, product_id)
);

CREATE TABLE IF NOT EXISTS public.product_variant_add_ons
(
    product_variant_id uuid NOT NULL,
    add_on_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.product_variant_policies
(
    product_variant_id uuid NOT NULL,
    policy_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.product_variant_roles
(
    product_variant_id uuid NOT NULL,
    role_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.product_variants
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    change_requested boolean DEFAULT false,
    code character varying(255) COLLATE pg_catalog."default",
    default_price double precision,
    extension boolean DEFAULT false,
    extension_of character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    plan_type integer,
    published boolean DEFAULT false,
    status integer,
    variant_attribute_type integer,
    variant_attribute_value character varying(255) COLLATE pg_catalog."default",
    application_id uuid,
    product_id uuid,
    CONSTRAINT product_variants_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.products
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    bundle boolean,
    code character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    plan boolean,
    plan_info text COLLATE pg_catalog."default",
    status integer,
    stripe_product_id character varying(255) COLLATE pg_catalog."default",
    subscribable boolean,
    type integer,
    product_group_id uuid,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.projects
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT projects_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.promotions
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default",
    end_date timestamp without time zone,
    max_redemptions integer,
    status integer,
    times_redeemed integer,
    coupon_id uuid,
    CONSTRAINT promotions_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.resource_scope_and_attribute
(
    id uuid NOT NULL,
    attributes character varying(255) COLLATE pg_catalog."default",
    policy_id uuid,
    resource_id uuid,
    scope_id uuid,
    CONSTRAINT resource_scope_and_attribute_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.resource_scopes
(
    resource_id uuid NOT NULL,
    scope_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.resources
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    code character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    module_id uuid,
    CONSTRAINT resources_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.role_policies
(
    role_id uuid NOT NULL,
    policy_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.roles
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    change_requested boolean DEFAULT false,
    code character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    project_id uuid,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.scope_actions
(
    scope_id uuid NOT NULL,
    action_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.scopes
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    change_requested boolean DEFAULT false,
    code character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT scopes_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.sub_app_user_roles
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    app_id uuid,
    role_id uuid,
    sub_id uuid,
    user_id uuid,
    CONSTRAINT sub_app_user_roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.subscription_apps
(
    subscription_id uuid NOT NULL,
    application_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.subscription_users
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    subscription_id uuid,
    user_id uuid,
    CONSTRAINT subscription_users_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.subscriptions
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    bizio_guest_subscription boolean DEFAULT false,
    extension boolean NOT NULL,
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    next_renewal_date date,
    occupied_seats integer DEFAULT 0,
    renew_next_subscription boolean DEFAULT false,
    subscription_status integer,
    subscription_type integer,
    valid_from date,
    valid_till date,
    extension_of uuid,
    order_id uuid,
    org_id uuid,
    preferred_billing_account_id uuid,
    price_id uuid,
    primary_user_id uuid,
    product_id uuid,
    product_variant_id uuid,
    promotion_id uuid,
    CONSTRAINT subscriptions_pkey PRIMARY KEY (id),
    CONSTRAINT uk_19bnl9weancplokifq0i5mapv UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS public.user_contract_resolved_rsa
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    attributes character varying(255) COLLATE pg_catalog."default",
    policy_code character varying(255) COLLATE pg_catalog."default",
    resource_code character varying(255) COLLATE pg_catalog."default",
    scope_code character varying(255) COLLATE pg_catalog."default",
    resolved_rsa_key uuid NOT NULL,
    CONSTRAINT user_contract_resolved_rsa_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.user_contracts
(
    app_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    org_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    resolved_rsa_key uuid,
    resolved_resource_scope_attrs text COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT user_contracts_pkey PRIMARY KEY (app_code, email, org_code),
    CONSTRAINT uk_o72gkjcvy2egmyivx7sdoi2xd UNIQUE (resolved_rsa_key)
);

CREATE TABLE IF NOT EXISTS public.user_preferences
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    country_locale character varying(255) COLLATE pg_catalog."default",
    date_format character varying(255) COLLATE pg_catalog."default",
    language character varying(255) COLLATE pg_catalog."default",
    number_format character varying(255) COLLATE pg_catalog."default",
    time_format character varying(255) COLLATE pg_catalog."default",
    time_zone character varying(255) COLLATE pg_catalog."default",
    user_id uuid,
    CONSTRAINT user_preferences_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    avatar character varying(255) COLLATE pg_catalog."default",
    designation character varying(255) COLLATE pg_catalog."default",
    dob date,
    email character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    gender integer,
    last_email_change_date timestamp without time zone,
    last_name character varying(255) COLLATE pg_catalog."default",
    last_password_change_date timestamp without time zone,
    middle_name character varying(255) COLLATE pg_catalog."default",
    oidc_id uuid,
    space_id character varying(255) COLLATE pg_catalog."default",
    status integer,
    stay_informed_about_bizio boolean DEFAULT false,
    terms_conditions_agreed boolean DEFAULT false,
    terms_conditions_agreed_timestamp timestamp without time zone,
    twofa boolean DEFAULT false,
    username character varying(255) COLLATE pg_catalog."default",
    organization_id uuid,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.variant_attributes
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    value character varying(255) COLLATE pg_catalog."default",
    attribute_code character varying(255) COLLATE pg_catalog."default",
    product_variant_id uuid,
    CONSTRAINT variant_attributes_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS public.verification_token_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.verification_token
(
    id bigint NOT NULL DEFAULT nextval('verification_token_id_seq'::regclass),
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by character varying(255) COLLATE pg_catalog."default",
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    expiry_date timestamp without time zone,
    payload text COLLATE pg_catalog."default",
    payload_class character varying(255) COLLATE pg_catalog."default",
    phone_number character varying(255) COLLATE pg_catalog."default",
    token character varying(255) COLLATE pg_catalog."default" NOT NULL,
    token_type integer,
    CONSTRAINT verification_token_pkey PRIMARY KEY (id),
    CONSTRAINT uk_p678btf3r9yu6u8aevyb4ff0m UNIQUE (token)
);

ALTER TABLE public.accounts
    DROP CONSTRAINT IF EXISTS fkp94qrum6paifd3n4j1h30esgb,
    ADD CONSTRAINT fkp94qrum6paifd3n4j1h30esgb FOREIGN KEY (primary_organization_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fkph2bgrslau4ioj3l3h3g79f2t,
    ADD CONSTRAINT fkph2bgrslau4ioj3l3h3g79f2t FOREIGN KEY (primary_contact_id)
        REFERENCES public.contacts (id);

ALTER TABLE public.addresses
    DROP CONSTRAINT IF EXISTS fk1fa36y2oqhao3wgg2rw1pi459,
    ADD CONSTRAINT fk1fa36y2oqhao3wgg2rw1pi459 FOREIGN KEY (user_id)
        REFERENCES public.users (id),
    DROP CONSTRAINT IF EXISTS fk21in4wl2ev1ciik3xa748tfgv,
    ADD CONSTRAINT fk21in4wl2ev1ciik3xa748tfgv FOREIGN KEY (acc_id)
        REFERENCES public.accounts (id),
    DROP CONSTRAINT IF EXISTS fk4j5d60gso8dfmo0osnkpeuu47,
    ADD CONSTRAINT fk4j5d60gso8dfmo0osnkpeuu47 FOREIGN KEY (org_id)
        REFERENCES public.organizations (id);

ALTER TABLE public.application_primary_user_roles
    DROP CONSTRAINT IF EXISTS fk4w3bgj6379gl399aatr2mu03t,
    ADD CONSTRAINT fk4w3bgj6379gl399aatr2mu03t FOREIGN KEY (application_id)
        REFERENCES public.applications (id),
    DROP CONSTRAINT IF EXISTS fkenanbvbinsht8tqurh1qnha2m,
    ADD CONSTRAINT fkenanbvbinsht8tqurh1qnha2m FOREIGN KEY (role_id)
        REFERENCES public.roles (id);

ALTER TABLE public.application_resources
    DROP CONSTRAINT IF EXISTS fk475j56fvjijuw7xvw2icjhxy3,
    ADD CONSTRAINT fk475j56fvjijuw7xvw2icjhxy3 FOREIGN KEY (application_id)
        REFERENCES public.applications (id),
    DROP CONSTRAINT IF EXISTS fk7l3eoarcyw5dbxk09heb4yqld,
    ADD CONSTRAINT fk7l3eoarcyw5dbxk09heb4yqld FOREIGN KEY (resource_id)
        REFERENCES public.resources (id);

ALTER TABLE public.applications
    DROP CONSTRAINT IF EXISTS fkhm18k2os8y6nqh80nv10g5cb6,
    ADD CONSTRAINT fkhm18k2os8y6nqh80nv10g5cb6 FOREIGN KEY (project_id)
        REFERENCES public.projects (id);

ALTER TABLE public.batch_job_execution
    DROP CONSTRAINT IF EXISTS job_inst_exec_fk,
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id)
        REFERENCES public.batch_job_instance (job_instance_id);

ALTER TABLE public.batch_job_execution_context
    DROP CONSTRAINT IF EXISTS job_exec_ctx_fk,
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id)
        REFERENCES public.batch_job_execution (job_execution_id);

ALTER TABLE public.batch_job_execution_params
    DROP CONSTRAINT IF EXISTS job_exec_params_fk,
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id)
        REFERENCES public.batch_job_execution (job_execution_id);

ALTER TABLE public.batch_step_execution
    DROP CONSTRAINT IF EXISTS job_exec_step_fk,
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id)
        REFERENCES public.batch_job_execution (job_execution_id);

ALTER TABLE public.batch_step_execution_context
    DROP CONSTRAINT IF EXISTS step_exec_ctx_fk,
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id)
        REFERENCES public.batch_step_execution (step_execution_id);

ALTER TABLE public.billing_accounts
    DROP CONSTRAINT IF EXISTS fk8ib0ua1jfxry2m8op3c3fq4sk,
    ADD CONSTRAINT fk8ib0ua1jfxry2m8op3c3fq4sk FOREIGN KEY (organization_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fkohihk2m6oudwox0hdsc9ra9ps,
    ADD CONSTRAINT fkohihk2m6oudwox0hdsc9ra9ps FOREIGN KEY (address_id)
        REFERENCES public.addresses (id);

ALTER TABLE public.bundle_items
    DROP CONSTRAINT IF EXISTS fk2tldwniqaqoicu7xqi38nci4f,
    ADD CONSTRAINT fk2tldwniqaqoicu7xqi38nci4f FOREIGN KEY (bundle_item_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fkjrpmdfqfqh1iyc78sj0ytaf6s,
    ADD CONSTRAINT fkjrpmdfqfqh1iyc78sj0ytaf6s FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id);

ALTER TABLE public.contacts
    DROP CONSTRAINT IF EXISTS fk456ftpyals0iqify7hihqrv6o,
    ADD CONSTRAINT fk456ftpyals0iqify7hihqrv6o FOREIGN KEY (org_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fk9kcyqv8ji8y0ts51gtj8pwva7,
    ADD CONSTRAINT fk9kcyqv8ji8y0ts51gtj8pwva7 FOREIGN KEY (acc_id)
        REFERENCES public.accounts (id),
    DROP CONSTRAINT IF EXISTS fkna8bddygr3l3kq1imghgcskt8,
    ADD CONSTRAINT fkna8bddygr3l3kq1imghgcskt8 FOREIGN KEY (user_id)
        REFERENCES public.users (id);

ALTER TABLE public.contract_change_requests
    DROP CONSTRAINT IF EXISTS fkmqfnp17bb0fg5pbdmli9ngdso,
    ADD CONSTRAINT fkmqfnp17bb0fg5pbdmli9ngdso FOREIGN KEY (contract_migration_id)
        REFERENCES public.contract_migrations (id);

ALTER TABLE public.invoices
    DROP CONSTRAINT IF EXISTS fk9llbmhhyycf6tbcsngok0xjh8,
    ADD CONSTRAINT fk9llbmhhyycf6tbcsngok0xjh8 FOREIGN KEY (price_id)
        REFERENCES public.prices (id),
    DROP CONSTRAINT IF EXISTS fkcmaap4plrm0ox5y4w64eymt9l,
    ADD CONSTRAINT fkcmaap4plrm0ox5y4w64eymt9l FOREIGN KEY (product_id)
        REFERENCES public.products (id),
    DROP CONSTRAINT IF EXISTS fkeggi7vrf8wt4ls0f7omhh6jk4,
    ADD CONSTRAINT fkeggi7vrf8wt4ls0f7omhh6jk4 FOREIGN KEY (address_id)
        REFERENCES public.addresses (id),
    DROP CONSTRAINT IF EXISTS fkekyltlmwbronx3ubn5it0346p,
    ADD CONSTRAINT fkekyltlmwbronx3ubn5it0346p FOREIGN KEY (subscription_id)
        REFERENCES public.subscriptions (id),
    DROP CONSTRAINT IF EXISTS fkn9bgoihpg014tdcjgu941ab7f,
    ADD CONSTRAINT fkn9bgoihpg014tdcjgu941ab7f FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fkq6fs19k0gqw3rg0mb87h60h6p,
    ADD CONSTRAINT fkq6fs19k0gqw3rg0mb87h60h6p FOREIGN KEY (payment_id)
        REFERENCES public.payments (id);

ALTER TABLE public.modules
    DROP CONSTRAINT IF EXISTS fkfo5b5gsihhu4kg302w24p96od,
    ADD CONSTRAINT fkfo5b5gsihhu4kg302w24p96od FOREIGN KEY (project_id)
        REFERENCES public.projects (id);

ALTER TABLE public.order_payloads
    DROP CONSTRAINT IF EXISTS fkr3g0r79ukjvwg8gavn7rywapm,
    ADD CONSTRAINT fkr3g0r79ukjvwg8gavn7rywapm FOREIGN KEY (order_id)
        REFERENCES public.orders (id);

ALTER TABLE public.orders
    DROP CONSTRAINT IF EXISTS fk42bki7v5u9s62olp5is82sd74,
    ADD CONSTRAINT fk42bki7v5u9s62olp5is82sd74 FOREIGN KEY (promotion_id)
        REFERENCES public.promotions (id),
    DROP CONSTRAINT IF EXISTS fk9kkve6bbmdqafoac1yrraxher,
    ADD CONSTRAINT fk9kkve6bbmdqafoac1yrraxher FOREIGN KEY (parent_organization_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fkc4w5pgfu97bvi5oowfu5naduo,
    ADD CONSTRAINT fkc4w5pgfu97bvi5oowfu5naduo FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fkhlglkvf5i60dv6dn397ethgpt,
    ADD CONSTRAINT fkhlglkvf5i60dv6dn397ethgpt FOREIGN KEY (address_id)
        REFERENCES public.addresses (id),
    DROP CONSTRAINT IF EXISTS fkkp5k52qtiygd8jkag4hayd0qg,
    ADD CONSTRAINT fkkp5k52qtiygd8jkag4hayd0qg FOREIGN KEY (product_id)
        REFERENCES public.products (id),
    DROP CONSTRAINT IF EXISTS fkmnqry320pwe9sb3kvil9r28f9,
    ADD CONSTRAINT fkmnqry320pwe9sb3kvil9r28f9 FOREIGN KEY (organization_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fkqf8iqrfxbbiu7c8v3fqovq0r7,
    ADD CONSTRAINT fkqf8iqrfxbbiu7c8v3fqovq0r7 FOREIGN KEY (price_id)
        REFERENCES public.prices (id);

ALTER TABLE public.org_join_requests
    DROP CONSTRAINT IF EXISTS fkss5m7sjh2qb91r040848a66qx,
    ADD CONSTRAINT fkss5m7sjh2qb91r040848a66qx FOREIGN KEY (org_id)
        REFERENCES public.organizations (id);

ALTER TABLE public.organization_users
    DROP CONSTRAINT IF EXISTS fk7yim0vro5cvwg60mb8qqbhb91,
    ADD CONSTRAINT fk7yim0vro5cvwg60mb8qqbhb91 FOREIGN KEY (organization_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fklw37px1u42owhtbry2mda9ujh,
    ADD CONSTRAINT fklw37px1u42owhtbry2mda9ujh FOREIGN KEY (user_id)
        REFERENCES public.users (id);

ALTER TABLE public.organizations
    DROP CONSTRAINT IF EXISTS fkgsay2unfbsax6k1ulw0k557o7,
    ADD CONSTRAINT fkgsay2unfbsax6k1ulw0k557o7 FOREIGN KEY (parent_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fknffutof86t4sc6attpjbbc43m,
    ADD CONSTRAINT fknffutof86t4sc6attpjbbc43m FOREIGN KEY (account_id)
        REFERENCES public.accounts (id);

ALTER TABLE public.payments
    DROP CONSTRAINT IF EXISTS fkt7s3auat9mamr3xu68e06wt8y,
    ADD CONSTRAINT fkt7s3auat9mamr3xu68e06wt8y FOREIGN KEY (billing_account_id)
        REFERENCES public.billing_accounts (id);

ALTER TABLE public.policies
    DROP CONSTRAINT IF EXISTS fk53go7do4hub9s4urtm8tylg9k,
    ADD CONSTRAINT fk53go7do4hub9s4urtm8tylg9k FOREIGN KEY (project_id)
        REFERENCES public.projects (id);

ALTER TABLE public.prices
    DROP CONSTRAINT IF EXISTS fkke2kp4krgsoovxfth1s00d7y5,
    ADD CONSTRAINT fkke2kp4krgsoovxfth1s00d7y5 FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id);

ALTER TABLE public.product_upgrade_orders
    DROP CONSTRAINT IF EXISTS fkjp27ccsrgtk5fu87emgs8bgcn,
    ADD CONSTRAINT fkjp27ccsrgtk5fu87emgs8bgcn FOREIGN KEY (pg_id)
        REFERENCES public.product_groups (id),
    DROP CONSTRAINT IF EXISTS fkt6vytvugle2jojddlphftogi0,
    ADD CONSTRAINT fkt6vytvugle2jojddlphftogi0 FOREIGN KEY (product_id)
        REFERENCES public.products (id);

ALTER TABLE public.product_variant_add_ons
    DROP CONSTRAINT IF EXISTS fkgn3l2bx7yqan4esdfjdeiyd03,
    ADD CONSTRAINT fkgn3l2bx7yqan4esdfjdeiyd03 FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fkj73t6a4cdonf35kfot3w7bu0m,
    ADD CONSTRAINT fkj73t6a4cdonf35kfot3w7bu0m FOREIGN KEY (add_on_id)
        REFERENCES public.product_variants (id);

ALTER TABLE public.product_variant_policies
    DROP CONSTRAINT IF EXISTS fk8wsb8e5d2b7uo85khf758jwpi,
    ADD CONSTRAINT fk8wsb8e5d2b7uo85khf758jwpi FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fkmc1c5jvyj21okakokpnwibbtk,
    ADD CONSTRAINT fkmc1c5jvyj21okakokpnwibbtk FOREIGN KEY (policy_id)
        REFERENCES public.policies (id);

ALTER TABLE public.product_variant_roles
    DROP CONSTRAINT IF EXISTS fkgmdhrsmlq625dnchxt99qyu8l,
    ADD CONSTRAINT fkgmdhrsmlq625dnchxt99qyu8l FOREIGN KEY (role_id)
        REFERENCES public.roles (id),
    DROP CONSTRAINT IF EXISTS fkqdr90usxf9wm657ehug92t4ix,
    ADD CONSTRAINT fkqdr90usxf9wm657ehug92t4ix FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id);

ALTER TABLE public.product_variants
    DROP CONSTRAINT IF EXISTS fkf7op7p61mtjvups1f19vpp4f5,
    ADD CONSTRAINT fkf7op7p61mtjvups1f19vpp4f5 FOREIGN KEY (application_id)
        REFERENCES public.applications (id),
    DROP CONSTRAINT IF EXISTS fkosqitn4s405cynmhb87lkvuau,
    ADD CONSTRAINT fkosqitn4s405cynmhb87lkvuau FOREIGN KEY (product_id)
        REFERENCES public.products (id);

ALTER TABLE public.products
    DROP CONSTRAINT IF EXISTS fkrmlc4hd8nhyq1bsmwbljo76mk,
    ADD CONSTRAINT fkrmlc4hd8nhyq1bsmwbljo76mk FOREIGN KEY (product_group_id)
        REFERENCES public.product_groups (id);

ALTER TABLE public.promotions
    DROP CONSTRAINT IF EXISTS fksukt3svg0ykmb1yrpp6jn6pik,
    ADD CONSTRAINT fksukt3svg0ykmb1yrpp6jn6pik FOREIGN KEY (coupon_id)
        REFERENCES public.coupons (id);

ALTER TABLE public.resource_scope_and_attribute
    DROP CONSTRAINT IF EXISTS fkeqmca08afwcnh79xqpq57k7i9,
    ADD CONSTRAINT fkeqmca08afwcnh79xqpq57k7i9 FOREIGN KEY (resource_id)
        REFERENCES public.resources (id),
    DROP CONSTRAINT IF EXISTS fkq2s7x7w4d391cp5ks2xse96rh,
    ADD CONSTRAINT fkq2s7x7w4d391cp5ks2xse96rh FOREIGN KEY (policy_id)
        REFERENCES public.policies (id),
    DROP CONSTRAINT IF EXISTS fktc6bsqjc3g1l1lyhjca7qp1c6,
    ADD CONSTRAINT fktc6bsqjc3g1l1lyhjca7qp1c6 FOREIGN KEY (scope_id)
        REFERENCES public.scopes (id);

ALTER TABLE public.resource_scopes
    DROP CONSTRAINT IF EXISTS fkcn8l6kg8ng2pamsgp948vs5ej,
    ADD CONSTRAINT fkcn8l6kg8ng2pamsgp948vs5ej FOREIGN KEY (scope_id)
        REFERENCES public.scopes (id),
    DROP CONSTRAINT IF EXISTS fkfjksjjcuka1wbw4lu4tjy618s,
    ADD CONSTRAINT fkfjksjjcuka1wbw4lu4tjy618s FOREIGN KEY (resource_id)
        REFERENCES public.resources (id);

ALTER TABLE public.resources
    DROP CONSTRAINT IF EXISTS fkqmtkjy1smy5gqh0mb1khq6v58,
    ADD CONSTRAINT fkqmtkjy1smy5gqh0mb1khq6v58 FOREIGN KEY (module_id)
        REFERENCES public.modules (id);

ALTER TABLE public.role_policies
    DROP CONSTRAINT IF EXISTS fk8l082f3k8skudvpbddnbd9ikb,
    ADD CONSTRAINT fk8l082f3k8skudvpbddnbd9ikb FOREIGN KEY (role_id)
        REFERENCES public.roles (id),
    DROP CONSTRAINT IF EXISTS fkdmcjlo7glr59tkojiyr3d5kxm,
    ADD CONSTRAINT fkdmcjlo7glr59tkojiyr3d5kxm FOREIGN KEY (policy_id)
        REFERENCES public.policies (id);

ALTER TABLE public.roles
    DROP CONSTRAINT IF EXISTS fkn2r9lxwnpqo2elh5qlj3dpuhx,
    ADD CONSTRAINT fkn2r9lxwnpqo2elh5qlj3dpuhx FOREIGN KEY (project_id)
        REFERENCES public.projects (id);

ALTER TABLE public.scope_actions
    DROP CONSTRAINT IF EXISTS fk3jyfuysjvb0aeq1excnci78bs,
    ADD CONSTRAINT fk3jyfuysjvb0aeq1excnci78bs FOREIGN KEY (scope_id)
        REFERENCES public.scopes (id),
    DROP CONSTRAINT IF EXISTS fkij0qrcdxi7rehm1op0pqkjk88,
    ADD CONSTRAINT fkij0qrcdxi7rehm1op0pqkjk88 FOREIGN KEY (action_id)
        REFERENCES public.actions (id);

ALTER TABLE public.sub_app_user_roles
    DROP CONSTRAINT IF EXISTS fk84klpic14gm13mnan7e6m3bic,
    ADD CONSTRAINT fk84klpic14gm13mnan7e6m3bic FOREIGN KEY (user_id)
        REFERENCES public.users (id),
    DROP CONSTRAINT IF EXISTS fk9wdexagly9bt1m6h3m6o5e3nf,
    ADD CONSTRAINT fk9wdexagly9bt1m6h3m6o5e3nf FOREIGN KEY (role_id)
        REFERENCES public.roles (id),
    DROP CONSTRAINT IF EXISTS fkcyobpmkb80f7sovk13ksl81ww,
    ADD CONSTRAINT fkcyobpmkb80f7sovk13ksl81ww FOREIGN KEY (app_id)
        REFERENCES public.applications (id),
    DROP CONSTRAINT IF EXISTS fkrs8140hg2if365j5w2pgyremb,
    ADD CONSTRAINT fkrs8140hg2if365j5w2pgyremb FOREIGN KEY (sub_id)
        REFERENCES public.subscriptions (id);

ALTER TABLE public.subscription_apps
    DROP CONSTRAINT IF EXISTS fkk1dq4xjqlfd76h059q8w0kt62,
    ADD CONSTRAINT fkk1dq4xjqlfd76h059q8w0kt62 FOREIGN KEY (application_id)
        REFERENCES public.applications (id),
    DROP CONSTRAINT IF EXISTS fkroiklhpn8o2uanar3e3sf63tu,
    ADD CONSTRAINT fkroiklhpn8o2uanar3e3sf63tu FOREIGN KEY (subscription_id)
        REFERENCES public.subscriptions (id);

ALTER TABLE public.subscription_users
    DROP CONSTRAINT IF EXISTS fkbaj2x8icv42yld0fgh79la3t9,
    ADD CONSTRAINT fkbaj2x8icv42yld0fgh79la3t9 FOREIGN KEY (user_id)
        REFERENCES public.users (id),
    DROP CONSTRAINT IF EXISTS fkfvvme5q1gnkcd3mwckweaulun,
    ADD CONSTRAINT fkfvvme5q1gnkcd3mwckweaulun FOREIGN KEY (subscription_id)
        REFERENCES public.subscriptions (id);

ALTER TABLE public.subscriptions
    DROP CONSTRAINT IF EXISTS fk7s6b99jlllla38r809x7c1wyt,
    ADD CONSTRAINT fk7s6b99jlllla38r809x7c1wyt FOREIGN KEY (preferred_billing_account_id)
        REFERENCES public.billing_accounts (id),
    DROP CONSTRAINT IF EXISTS fk8bgqqpwcr25magbcxvo8j393j,
    ADD CONSTRAINT fk8bgqqpwcr25magbcxvo8j393j FOREIGN KEY (price_id)
        REFERENCES public.prices (id),
    DROP CONSTRAINT IF EXISTS fkakghexsb3ddy1sowxtob7jl12,
    ADD CONSTRAINT fkakghexsb3ddy1sowxtob7jl12 FOREIGN KEY (order_id)
        REFERENCES public.orders (id),
    DROP CONSTRAINT IF EXISTS fkcphk3tpyla6eqv0v1lua3agwa,
    ADD CONSTRAINT fkcphk3tpyla6eqv0v1lua3agwa FOREIGN KEY (primary_user_id)
        REFERENCES public.users (id),
    DROP CONSTRAINT IF EXISTS fkh94mar5jgxd8ijp8be99m07a2,
    ADD CONSTRAINT fkh94mar5jgxd8ijp8be99m07a2 FOREIGN KEY (extension_of)
        REFERENCES public.subscriptions (id),
    DROP CONSTRAINT IF EXISTS fkhutpda3md8b9s2cnmkdm2rpep,
    ADD CONSTRAINT fkhutpda3md8b9s2cnmkdm2rpep FOREIGN KEY (product_id)
        REFERENCES public.products (id),
    DROP CONSTRAINT IF EXISTS fkk1lcpjransbupkfwmgg7i98nf,
    ADD CONSTRAINT fkk1lcpjransbupkfwmgg7i98nf FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fkp1af8ttyag997jalwpay7fhtj,
    ADD CONSTRAINT fkp1af8ttyag997jalwpay7fhtj FOREIGN KEY (org_id)
        REFERENCES public.organizations (id),
    DROP CONSTRAINT IF EXISTS fksmsi9ocdvwm16d8o14untprsy,
    ADD CONSTRAINT fksmsi9ocdvwm16d8o14untprsy FOREIGN KEY (promotion_id)
        REFERENCES public.promotions (id);

ALTER TABLE public.user_contract_resolved_rsa
    DROP CONSTRAINT IF EXISTS fk6cxv5gp49rb7gxptf6veyabki,
    ADD CONSTRAINT fk6cxv5gp49rb7gxptf6veyabki FOREIGN KEY (resolved_rsa_key)
        REFERENCES public.user_contracts (resolved_rsa_key);

ALTER TABLE public.user_preferences
    DROP CONSTRAINT IF EXISTS fkepakpib0qnm82vmaiismkqf88,
    ADD CONSTRAINT fkepakpib0qnm82vmaiismkqf88 FOREIGN KEY (user_id)
        REFERENCES public.users (id);

ALTER TABLE public.users
    DROP CONSTRAINT IF EXISTS fkqpugllwvyv37klq7ft9m8aqxk,
    ADD CONSTRAINT fkqpugllwvyv37klq7ft9m8aqxk FOREIGN KEY (organization_id)
        REFERENCES public.organizations (id);

ALTER TABLE public.variant_attributes
    DROP CONSTRAINT IF EXISTS fk1h5puex8jvbp020u9sywnm4mb,
    ADD CONSTRAINT fk1h5puex8jvbp020u9sywnm4mb FOREIGN KEY (product_variant_id)
        REFERENCES public.product_variants (id),
    DROP CONSTRAINT IF EXISTS fk93kvdtma4dgxpqiplt9v8i5u1,
    ADD CONSTRAINT fk93kvdtma4dgxpqiplt9v8i5u1 FOREIGN KEY (attribute_code)
        REFERENCES public.attributes (code);
