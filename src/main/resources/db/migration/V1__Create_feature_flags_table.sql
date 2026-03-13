CREATE TABLE feature_flags (
                               id UUID PRIMARY KEY,
                               name VARCHAR(100) NOT NULL,
                               environment VARCHAR(50) NOT NULL,
                               enabled BOOLEAN NOT NULL,
                               rollout_percentage INT NOT NULL,

                               created_at TIMESTAMP,
                               updated_at TIMESTAMP
);