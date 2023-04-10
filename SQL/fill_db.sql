-- Add some sample persons
INSERT INTO persons (first_name, name, address) VALUES
    ('John', 'Doe', '123 Main St'),
    ('Jane', 'Smith', '456 Oak Ave'),
    ('Bob', 'Johnson', '789 Maple Rd');

-- Add some sample apartments
INSERT INTO apartments (city, postal_code, street, street_number, square_area, floor, rent, rooms, balcony, built_in_kitchen) VALUES
    ('New York', '10001', 'Broadway', '123', 1000, 5, 2000.0, 3, true, true),
    ('Los Angeles', '90001', 'Hollywood Blvd', '456', 750, 2, 1500.0, 2, false, false),
    ('Chicago', '60601', 'Michigan Ave', '789', 1200, 10, 3000.0, 4, true, true);

-- Add some sample houses
INSERT INTO houses (city, postal_code, street, street_number, square_area, floors, price, garden) VALUES
    ('New York', '10001', 'Broadway', '123', 1000, 2, 500000.0, true),
    ('Los Angeles', '90001', 'Hollywood Blvd', '456', 750, 3, 750000.0, false),
    ('Chicago', '60601', 'Michigan Ave', '789', 1200, 4, 1000000.0, true);

