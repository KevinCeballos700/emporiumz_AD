const { pool } = require("../config/db");

async function createOrder(userId, total, address, paymentMethod, items) {
  const conn = await pool.getConnection();
  try {
    await conn.beginTransaction();
    const [res] = await conn.query(
      "INSERT INTO orders (user_id, total, address, payment_method) VALUES (?, ?, ?, ?)",
      [userId, total, address, paymentMethod]
    );
    const orderId = res.insertId;
    for (const it of items) {
      await conn.query("INSERT INTO order_items (order_id, sku, qty, unit_price) VALUES (?, ?, ?, ?)",
        [orderId, it.sku, it.qty, it.unitPrice]);
      await conn.query("UPDATE products SET stock = stock - ? WHERE sku = ?", [it.qty, it.sku]);
    }
    await conn.commit();
    return { orderId };
  } catch (err) {
    await conn.rollback();
    throw err;
  } finally { conn.release(); }
}

module.exports = { createOrder };
