CREATE TABLE IF NOT EXISTS "local_to_put"(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_created TIMESTAMP NOT NULL,    
    date_updated TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "module_father"(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "module_child"(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    id_module_father BIGINT NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,
    CONSTRAINT fk_module_father FOREIGN KEY (id_module_father) REFERENCES module_father(id)
);

CREATE TABLE IF NOT EXISTS "products"(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type_product VARCHAR(255) NOT NULL,
    id_local_to_put BIGINT NOT NULL,
    id_module_father BIGINT NOT NULL,
    id_module_child BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,
    CONSTRAINT fk_local_to_put FOREIGN KEY (id_local_to_put) REFERENCES local_to_put(id),
    CONSTRAINT fk_module_father FOREIGN KEY (id_module_father) REFERENCES module_father(id),
    CONSTRAINT fk_module_child FOREIGN KEY (id_module_child) REFERENCES module_child(id)
);

CREATE TABLE IF NOT EXISTS "sizes"(
    id BIGSERIAL PRIMARY KEY,
    id_product BIGINT NOT NULL,
    height_max DOUBLE PRECISION NOT NULL,
    height_min DOUBLE PRECISION NOT NULL,
    width_max DOUBLE PRECISION NOT NULL,
    width_min DOUBLE PRECISION NOT NULL,
    depth_max DOUBLE PRECISION NOT NULL,
    depth_min DOUBLE PRECISION NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,
    CONSTRAINT fk_product FOREIGN KEY (id_product) REFERENCES products(id)
);
