CREATE TABLE IF NOT EXISTS persons (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE estate_agents (
    name varchar(255),
    address varchar(255),
    login varchar(40) UNIQUE,
    password varchar(40),
    id serial primary key
);

CREATE TABLE IF NOT EXISTS estates (
    id SERIAL PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    postal_code INTEGER NOT NULL,
    street VARCHAR(255) NOT NULL,
    street_number VARCHAR(255) NOT NULL,
    square_area FLOAT NOT NULL,
    agent_id INTEGER REFERENCES estate_agents(id)
);

CREATE TABLE IF NOT EXISTS apartments (
    floor INTEGER NOT NULL,
    rent FLOAT NOT NULL,
    rooms INTEGER NOT NULL,
    balcony BOOLEAN NOT NULL,
    built_in_kitchen BOOLEAN NOT NULL
) INHERITS (estates);

CREATE TABLE IF NOT EXISTS houses (
    floors INTEGER NOT NULL,
    price FLOAT NOT NULL,
    garden BOOLEAN NOT NULL
) INHERITS (estates);

CREATE TABLE IF NOT EXISTS contracts (
    contract_number SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    place VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS purchase_contracts (
    num_installments INTEGER NOT NULL,
    interest_rate FLOAT NOT NULL
) INHERITS (contracts);

CREATE TABLE IF NOT EXISTS tenancy_contracts (
    start_date DATE NOT NULL,
    duration INTERVAL NOT NULL,
    additional_costs NUMERIC NOT NULL
) INHERITS (contracts);

CREATE TABLE IF NOT EXISTS rents (
    apartment_id INTEGER REFERENCES estates(id),
    contract_id INTEGER NOT NULL REFERENCES contracts(contract_number),
    tenant_id INTEGER REFERENCES persons(id)
);

CREATE TABLE IF NOT EXISTS sells (
    house_id INTEGER REFERENCES estates(id),
    contract_id INTEGER NOT NULL REFERENCES contracts(contract_number),
    seller INTEGER REFERENCES persons(id)
);
