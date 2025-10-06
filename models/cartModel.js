const { pool } = require("../config/db");

async function upsertCartItem(userId, sku, qty) {
  const conn = await pool.getConnection();
  try {
    await conn.beginTransaction();
    const [exists] = await conn.query("SELECT id, qty FROM cart_items WHERE user_id=? AND sku=?", [userId, sku]);
    if (exists.length) {
      const newQty = exists[0].qty + qty;
      await conn.query("UPDATE cart_items SET qty=? WHERE id=?", [newQty, exists[0].id]);
    } else {
      await conn.query("INSERT INTO cart_items (user_id, sku, qty) VALUES (?, ?, ?)", [userId, sku, qty]);
    }
    await conn.commit();
    return true;
  } catch (err) {
    await conn.rollback();
    throw err;
  } finally { conn.release(); }
}

async function getCartByUser(userId) {
  const [rows] = await pool.query(
    `SELECT ci.id, ci.user_id, ci.sku, ci.qty, p.name, p.price, p.img_url
     FROM cart_items ci
     LEFT JOIN products p ON p.sku = ci.sku
     WHERE ci.user_id = ?`, [userId]
  );
  return rows;
}

async function clearCart(userId) {
  await pool.query("DELETE FROM cart_items WHERE user_id = ?", [userId]);
}

module.exports = { upsertCartItem, getCartByUser, clearCart };
