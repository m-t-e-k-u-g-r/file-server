ALTER TABLE access_key
ADD CONSTRAINT access_key_file_fk
FOREIGN KEY (file_id) REFERENCES file (id);
