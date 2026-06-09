ALTER TABLE iot_camera_record
    ADD COLUMN upload_status tinyint NOT NULL DEFAULT 0 COMMENT 'Upload status: 0 pending, 1 success, 2 failed',
    ADD COLUMN upload_time datetime NULL COMMENT 'Upload finished time',
    ADD COLUMN upload_error_msg varchar(500) NULL COMMENT 'Upload failure reason';
