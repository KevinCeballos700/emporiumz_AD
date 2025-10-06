CREATE DATABASE IF NOT EXISTS emporiumz_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE emporiumz_db;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  contact VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) DEFAULT 'user',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
  sku VARCHAR(50) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  img_url TEXT,
  stock INT DEFAULT 0,
  reorder INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS cart_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  sku VARCHAR(50) NOT NULL,
  qty INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY user_sku_unique (user_id, sku),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (sku) REFERENCES products(sku) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  total INT NOT NULL,
  address TEXT,
  payment_method VARCHAR(50),
  status VARCHAR(50) DEFAULT 'pending',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS order_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  sku VARCHAR(50) NOT NULL,
  qty INT NOT NULL,
  unit_price INT NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (sku) REFERENCES products(sku) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS tracking (
  tracking_number VARCHAR(50) PRIMARY KEY,
  order_id INT,
  status VARCHAR(100),
  last_update TIMESTAMP,
  details JSON,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE SET NULL
);

INSERT IGNORE INTO products (sku, name, price, img_url, stock, reorder) VALUES
('camisapolo', 'Camisa Polo', 35000, '/imagenes/Camisapolo.jpg', 50, 5),
('iphone12', 'IPhone 12', 199900, '/imagenes/Iphone12mini.jpg', 20, 2),
('adidascopa3', 'Adidas Copa 3', 280000, '/imagenes/adidasguayos.png', 30, 3);
