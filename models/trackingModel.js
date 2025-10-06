const { pool } = require("../config/db");

async function getTracking(trackingNumber) {
  const [rows] = await pool.query("SELECT * FROM tracking WHERE tracking_number = ?", [trackingNumber]);
  return rows[0];
}

module.exports = { getTracking };
