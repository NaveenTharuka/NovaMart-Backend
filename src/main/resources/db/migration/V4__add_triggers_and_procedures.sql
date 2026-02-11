-- PostgreSQL triggers to update cart total price when cart items change

-- Function to update cart total price
CREATE OR REPLACE FUNCTION update_cart_total_price()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the cart total price based on the sum of cart items
    UPDATE cart c
    SET total_price = (
        SELECT COALESCE(SUM(ci.subtotal), 0)
        FROM cart_items ci
        WHERE ci.cart_id = COALESCE(NEW.cart_id, OLD.cart_id)
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE c.id = COALESCE(NEW.cart_id, OLD.cart_id);
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Trigger for INSERT on cart_items
CREATE TRIGGER trg_cart_items_insert
AFTER INSERT ON cart_items
FOR EACH ROW
EXECUTE FUNCTION update_cart_total_price();

-- Trigger for UPDATE on cart_items
CREATE TRIGGER trg_cart_items_update
AFTER UPDATE ON cart_items
FOR EACH ROW
EXECUTE FUNCTION update_cart_total_price();

-- Trigger for DELETE on cart_items
CREATE TRIGGER trg_cart_items_delete
AFTER DELETE ON cart_items
FOR EACH ROW
EXECUTE FUNCTION update_cart_total_price();

-- Function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply updated_at trigger to all tables
CREATE TRIGGER trg_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_category_updated_at
BEFORE UPDATE ON category
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_products_updated_at
BEFORE UPDATE ON products
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_cart_updated_at
BEFORE UPDATE ON cart
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_cart_items_updated_at
BEFORE UPDATE ON cart_items
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_orders_updated_at
BEFORE UPDATE ON orders
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_order_items_updated_at
BEFORE UPDATE ON order_items
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_reviews_updated_at
BEFORE UPDATE ON reviews
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();