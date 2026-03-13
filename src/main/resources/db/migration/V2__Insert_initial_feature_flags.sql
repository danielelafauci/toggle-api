INSERT INTO feature_flags (id, name, environment, enabled, rollout_percentage, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'nuova-ui', 'produzione', true, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'nuovo-checkout', 'produzione', false, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);