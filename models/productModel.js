const { pool } = require("../config/db");

async function getAllProducts() {
  const [rows] = await pool.query("SELECT * FROM products");
  return rows;
}
async function getProductBySku(sku) {
  const [rows] = await pool.query("SELECT * FROM products WHERE sku = ?", [sku]);
  return rows[0];
}
module.exports = { getAllProducts, getProductBySku };
