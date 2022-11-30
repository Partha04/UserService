CREATE SEQUENCE  IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
  id UUID NOT NULL,
   user_detail_id UUID,
   email VARCHAR(255),
   password VARCHAR(255),
   active BOOLEAN,
   role INTEGER,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE contacts (
  id BIGINT NOT NULL,
   country_code VARCHAR(255),
   value VARCHAR(255),
   CONSTRAINT pk_contacts PRIMARY KEY (id)
);

CREATE TABLE address (
  id BIGINT NOT NULL,
   line1 VARCHAR(255),
   line2 VARCHAR(255),
   land_mark VARCHAR(255),
   district VARCHAR(255),
   state VARCHAR(255),
   pin_code VARCHAR(255),
   CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE user_detail (
  id UUID NOT NULL,
   first_name VARCHAR(255),
   last_name VARCHAR(255),
   CONSTRAINT pk_user_detail PRIMARY KEY (id)
);
CREATE TABLE user_detail_contacts (
  user_detail_id UUID NOT NULL,
   contacts_id BIGINT NOT NULL
);
CREATE TABLE user_detail_addresses (
  user_detail_id UUID NOT NULL,
   addresses_id BIGINT NOT NULL
);

ALTER TABLE user_detail_contacts ADD CONSTRAINT uc_user_detail_contacts_contacts UNIQUE (contacts_id);

ALTER TABLE user_detail_contacts ADD CONSTRAINT fk_usedetcon_on_contact FOREIGN KEY (contacts_id) REFERENCES contacts (id);

ALTER TABLE user_detail_contacts ADD CONSTRAINT fk_usedetcon_on_user_detail FOREIGN KEY (user_detail_id) REFERENCES user_detail (id);

ALTER TABLE user_detail_addresses ADD CONSTRAINT uc_user_detail_addresses_addresses UNIQUE (addresses_id);

ALTER TABLE user_detail_addresses ADD CONSTRAINT fk_usedetadd_on_address FOREIGN KEY (addresses_id) REFERENCES address (id);

ALTER TABLE user_detail_addresses ADD CONSTRAINT fk_usedetadd_on_user_detail FOREIGN KEY (user_detail_id) REFERENCES user_detail (id);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users ADD CONSTRAINT FK_USERS_ON_USERDETAIL FOREIGN KEY (user_detail_id) REFERENCES user_detail (id);