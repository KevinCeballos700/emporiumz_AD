const { pool } = require("../config/db");
const { success, error } = require("../utils/responses");

async function list(req, res) {
  try {
    const [rows] = await pool.query("SELECT * FROM products");
    return success(res, rows);
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

module.exports = { list };
