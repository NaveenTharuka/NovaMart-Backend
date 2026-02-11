-- V3__insert_initial_data.sql
-- Insert initial data with proper foreign key relationships

-- 1. Insert categories (table name is 'category', not 'categories')
INSERT INTO category (name) VALUES
    ('Electronics'),
    ('Clothing'),
    ('Books'),
    ('Home & Kitchen'),
    ('Sports & Outdoors')
ON CONFLICT DO NOTHING;

-- 2. Insert admin user
-- Password: 'admin123' (you should hash this in your application)
INSERT INTO users (id, username, email, password, phone_number, address, role) VALUES
    (
        gen_random_uuid(),
        'admin',
        'admin@novamart.com',
        '$2a$10$YourHashedPasswordHere', -- Use BCrypt: encoder.encode("admin123")
        1234567890,
        'Admin Address',
        'ADMIN'
    )
ON CONFLICT (email) DO NOTHING;

-- 3. Insert sample products
-- Note: Get category IDs from the inserted categories
INSERT INTO products (id, name, description, price, quantity, img_url, category_id) VALUES
    (
        gen_random_uuid(),
        'Laptop',
        'High-performance laptop with 16GB RAM and 512GB SSD',
        999.99,
        50,
        '/images/laptop.jpg',
        (SELECT id FROM category WHERE name = 'Electronics' LIMIT 1)
    ),
    (
        gen_random_uuid(),
        'T-Shirt',
        'Premium cotton T-Shirt in various colors and sizes',
        19.99,
        100,
        '/images/tshirt.jpg',
        (SELECT id FROM category WHERE name = 'Clothing' LIMIT 1)
    ),
    (
        gen_random_uuid(),
        'Novel',
        'Bestseller fiction novel by award-winning author',
        12.99,
        200,
        '/images/novel.jpg',
        (SELECT id FROM category WHERE name = 'Books' LIMIT 1)
    )
ON CONFLICT DO NOTHING;

-- Optional: Insert a regular user for testing
INSERT INTO users (id, username, email, password, phone_number, address, role) VALUES
    (
        gen_random_uuid(),
        'john_doe',
        'john.doe@example.com',
        '$2a$10$YourHashedPasswordHere', -- Use BCrypt: encoder.encode("password")
        9876543210,
        '123 Main Street, Cityville',
        'USER'
    )
ON CONFLICT (email) DO NOTHING;