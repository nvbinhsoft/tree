-- Ensure post body/excerpt are stored as text (not bytea) to support lower() search
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'posts' AND column_name = 'body' AND data_type = 'bytea'
  ) THEN
    EXECUTE 'ALTER TABLE posts ALTER COLUMN body TYPE text USING convert_from(body, ''UTF8'')';
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'posts' AND column_name = 'excerpt' AND data_type = 'bytea'
  ) THEN
    EXECUTE 'ALTER TABLE posts ALTER COLUMN excerpt TYPE text USING convert_from(excerpt, ''UTF8'')';
  END IF;
END $$;
