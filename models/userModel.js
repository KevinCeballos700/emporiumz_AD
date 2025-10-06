const { pool } = require("../config/db");

async function createUser(fullName, contact, passwordHash, role="user") {
  const [result] = await pool.query(
    "INSERT INTO users (full_name, contact, password_hash, role) VALUES (?, ?, ?, ?)",
    [fullName, contact, passwordHash, role]
  );
  return { id: result.insertId, fullName, contact, role };
}

async function findUserByContact(contact) {
  const [rows] = await pool.query("SELECT * FROM users WHERE contact = ?", [contact]);
  return rows[0];
}

async function findUserById(id) {
  const [rows] = await pool.query("SELECT * FROM users WHERE id = ?", [id]);
  return rows[0];
}

module.exports = { createUser, findUserByContact, findUserById };
