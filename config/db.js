const mysql = require("mysql2/promise");
const dotenv = require("dotenv");
const fs = require("fs");
const path = require("path");
dotenv.config();

const pool = mysql.createPool({
  host: process.env.DB_HOST || "localhost",
  port: process.env.DB_PORT || 3306,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

async function runMigrations() {
  const sql = fs.readFileSync(path.join(__dirname, "..", "..", "db", "migrate.sql"), "utf8");
  const conn = await pool.getConnection();
  try {
    await conn.query(sql);
    console.log("Migraciones ejecutadas");
  } finally {
    conn.release();
    process.exit(0);
  }
}

module.exports = { pool, runMigrations };
