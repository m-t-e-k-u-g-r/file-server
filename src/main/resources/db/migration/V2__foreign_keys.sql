ALTER TABLE access_key
ADD CONSTRAINT access_key_file_fk
FOREIGN KEY (file_id) REFERENCES file (id)
ON DELETE CASCADE;

ALTER TABLE log_entry
ADD CONSTRAINT log_access_key_fk
FOREIGN KEY (access_key_id) REFERENCES access_key (id);

ALTER TABLE log_entry
ADD CONSTRAINT log_file_fk
FOREIGN KEY (file_id) REFERENCES file (id);
