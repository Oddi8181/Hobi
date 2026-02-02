
CREATE TABLE token (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_user UUID NOT NULL,
    token_hash VARCHAR(800) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES "user"(id)
);

CREATE TABLE notification (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sender UUID,
    id_receiver UUID,
    subject VARCHAR(255),
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender FOREIGN KEY (id_sender) REFERENCES "user"(id),
    CONSTRAINT fk_receiver FOREIGN KEY (id_receiver) REFERENCES "user"(id)
);

ALTER TABLE content_like DROP CONSTRAINT IF EXISTS unique_like;
ALTER TABLE content_like DROP CONSTRAINT IF EXISTS content_like_id_post_id_user_id_comment_key;

CREATE UNIQUE INDEX unique_post_like
    ON content_like (id_post, id_user)
    WHERE id_comment IS NULL AND id_post IS NOT NULL;

CREATE UNIQUE INDEX unique_comment_like
    ON content_like (id_comment, id_user)
    WHERE id_post IS NULL AND id_comment IS NOT NULL;