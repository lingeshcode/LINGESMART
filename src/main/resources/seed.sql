-- Seed Data for LingeshMart
-- Password for all seed accounts is: password123 (hashed using jBCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy)

-- 1. Users (Admin, Sellers, Buyers)
INSERT INTO users (id, name, email, password_hash, role) VALUES
(1, 'Lingesh Admin', 'admin@lingeshmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
(2, 'TechZone Electronics', 'seller1@lingeshmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SELLER'),
(3, 'StyleHub Fashion', 'seller2@lingeshmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SELLER'),
(4, 'John Buyer', 'buyer1@lingeshmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'BUYER'),
(5, 'Priya Sharma', 'buyer2@lingeshmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'BUYER');

-- 2. Products
INSERT INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) VALUES
(1, 2, 'iPhone 15 Pro Max (256 GB)', 'Titanium design, A17 Pro chip, 48MP main camera with 5x optical zoom.', 134900.00, 15, 'Electronics', 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600'),
(2, 2, 'MacBook Air M3 (16GB, 512GB)', 'Liquid Retina display, MagSafe 3 charging, supercharged by M3 chip.', 114900.00, 10, 'Electronics', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600'),
(3, 2, 'Sony WH-1000XM5 Headphones', 'Industry leading noise canceling wireless headphones with Auto NC Optimizer.', 29990.00, 25, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600'),
(4, 3, 'Men Leather Biker Jacket', '100% Genuine lambskin leather jacket with premium zip detailing.', 4999.00, 40, 'Fashion', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600'),
(5, 3, 'Nike Air Zoom Running Shoes', 'Lightweight responsive cushioning for daily running.', 6995.00, 50, 'Fashion', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600'),
(6, 2, 'Samsung 55-inch Crystal 4K TV', 'PurColor, Crystal Processor 4K, Motion Xcelerator and Q-Symphony sound.', 42990.00, 8, 'Home', 'https://images.unsplash.com/photo-1593784991095-a205069470b6?w=600'),
(7, 3, 'Ergonomic Mesh Office Chair', 'Adjustable lumbar support, 3D armrests, heavy duty metal base for long working hours.', 7999.00, 30, 'Home', 'https://images.unsplash.com/photo-1580481072645-022f9a6d1270?w=600'),
(8, 2, 'Clean Code by Robert C. Martin', 'A Handbook of Agile Software Craftsmanship - essential reading for developers.', 899.00, 100, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600');

-- 3. Sample Orders
INSERT INTO orders (id, buyer_id, status, total_amount, created_at) VALUES
(1, 4, 'DELIVERED', 34989.00, CURRENT_TIMESTAMP - INTERVAL '5' DAY),
(2, 4, 'SHIPPED', 114900.00, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
(3, 5, 'CONFIRMED', 4999.00, CURRENT_TIMESTAMP - INTERVAL '1' DAY);

-- 4. Order Items
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES
(1, 1, 3, 1, 29990.00),
(2, 1, 4, 1, 4999.00),
(3, 2, 2, 1, 114900.00),
(4, 3, 4, 1, 4999.00);

-- 5. Reviews
INSERT INTO reviews (id, product_id, user_id, rating, comment) VALUES
(1, 3, 4, 5, 'Unbelievable noise cancellation! Perfect for studying and commuting.'),
(2, 4, 4, 4, 'Great genuine leather feel and super comfy fit.'),
(3, 2, 4, 5, 'Blazing fast performance and battery life lasts 2 full days!');

-- 6. Wishlist
INSERT INTO wishlist (id, user_id, product_id) VALUES
(1, 4, 1),
(2, 5, 2);
