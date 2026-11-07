ALTER TABLE answers ALTER COLUMN users_id SET NOT NULL;
ALTER TABLE classes ADD CONSTRAINT classes_name_unique UNIQUE ("name");
