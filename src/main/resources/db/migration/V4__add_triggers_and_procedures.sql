-- Create trigger to update cart total price when cart items change
DELIMITER $$
CREATE TRIGGER update_cart_total
    AFTER INSERT ON cart_items
    FOR EACH ROW
BEGIN
    UPDATE cart c
    SET c.total_price = (
        SELECT COALESCE(SUM(ci.subtotal), 0)
        FROM cart_items ci
        WHERE ci.cart_id = NEW.cart_id
    )
    WHERE c.id = NEW.cart_id;
    END$$

    CREATE TRIGGER update_cart_total_update
        AFTER UPDATE ON cart_items
        FOR EACH ROW
    BEGIN
        UPDATE cart c
        SET c.total_price = (
            SELECT COALESCE(SUM(ci.subtotal), 0)
            FROM cart_items ci
            WHERE ci.cart_id = NEW.cart_id
        )
        WHERE c.id = NEW.cart_id;
        END$$

        CREATE TRIGGER update_cart_total_delete
            AFTER DELETE ON cart_items
            FOR EACH ROW
        BEGIN
            UPDATE cart c
            SET c.total_price = (
                SELECT COALESCE(SUM(ci.subtotal), 0)
                FROM cart_items ci
                WHERE ci.cart_id = OLD.cart_id
            )
            WHERE c.id = OLD.cart_id;
            END$$
            DELIMITER ;