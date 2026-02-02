ALTER TABLE friendship
ALTER COLUMN date_of_befriending TYPE timestamp USING date_of_befriending::timestamp;

ALTER TABLE comment
ADD COLUMN id_user UUID,
ADD CONSTRAINT fk_comment_user
    FOREIGN KEY (id_user)
    REFERENCES "user"(id);

ALTER TABLE message
ALTER COLUMN message TYPE VARCHAR(800);