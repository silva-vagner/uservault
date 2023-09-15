
CREATE TABLE user_audit (
    user_audit_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    content_json VARCHAR(255),
    created_by UUID,
    created_date TIMESTAMP,
    last_updated_by UUID,
    last_updated_date TIMESTAMP,
    action VARCHAR(255),
    user_id UUID,
    CONSTRAINT FK_user_created_by_on_user_audit
    FOREIGN KEY (created_by)
        REFERENCES "user"(user_id),
    CONSTRAINT FK_user_last_updated_by_on_user_audit
    FOREIGN KEY (last_updated_by)
        REFERENCES "user"(user_id),
    CONSTRAINT FK_user_user_id_on_user_audit
    FOREIGN KEY (user_id)
        REFERENCES "user"(user_id)
);