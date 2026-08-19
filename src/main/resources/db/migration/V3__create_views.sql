CREATE OR REPLACE VIEW log_overview AS
SELECT
    le.id,
    le.received AS access_time,
    f.original_filename AS file_name,
    ak.description AS key_description,
    le.authorized,
    le.matches AS key_valid,
    le.expired AS key_expired,
    le.revoked AS key_revoked
FROM log_entry le
    LEFT JOIN access_key ak on ak.id = le.access_key_id
    LEFT JOIN file f ON f.id = le.file_id;
