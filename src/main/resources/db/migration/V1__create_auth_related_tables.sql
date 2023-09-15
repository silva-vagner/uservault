
CREATE TABLE "user" (
  user_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE "role" (
  role_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE user_role (
  user_id UUID REFERENCES "user"(user_id),
  role_id UUID REFERENCES "role"(role_id),
  PRIMARY KEY (user_id, role_id)
);
