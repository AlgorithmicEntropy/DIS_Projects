-- Add some sample agents
INSERT INTO estate_agents (name, address, login, password) VALUES
    ('Test Agent', '123 Main St', 'test', 'test');

-- Add some sample persons
INSERT INTO persons (first_name, name, address) VALUES
    ('John', 'Doe', '123 Main St'),
    ('Jane', 'Smith', '456 Oak Ave'),
    ('Bob', 'Johnson', '789 Maple Rd');

-- Add some sample apartments
INSERT INTO apartments (city, postal_code, street, street_number, square_area, floor, rent, rooms, balcony, built_in_kitchen, agent_id) VALUES
    ('New York', '10001', 'Broadway', '123', 1000, 5, 2000.0, 3, true, true, 1),
    ('Los Angeles', '90001', 'Hollywood Blvd', '456', 750, 2, 1500.0, 2, false, false, 1),
    ('Chicago', '60601', 'Michigan Ave', '789', 1200, 10, 3000.0, 4, true, true, 1);

-- Add some sample houses
INSERT INTO houses (city, postal_code, street, street_number, square_area, floors, price, garden, agent_id) VALUES
    ('New York', '10001', 'Broadway', '123', 1000, 2, 500000.0, true, 1),
    ('Los Angeles', '90001', 'Hollywood Blvd', '456', 750, 3, 750000.0, false, 1),
    ('Chicago', '60601', 'Michigan Ave', '789', 1200, 4, 1000000.0, true, 1);

