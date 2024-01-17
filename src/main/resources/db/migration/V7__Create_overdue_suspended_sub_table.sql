create materialized view if not exists ots_subscriptions as (
    select
        sub.id as sub_id,
        org.code as org_code,
        org.id as org_id,
        sub.subscription_status as status,
        u.username as username
    from subscriptions sub
    join organizations org on sub.org_id = org.id
    join subscription_users su on su.subscription_id = sub.id
    join users u on u.id = su.user_id
    where sub.subscription_status in (1,2,3) and u.status =
);