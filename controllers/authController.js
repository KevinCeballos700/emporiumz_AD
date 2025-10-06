const { createUser, findUserByContact } = require("../models/userModel");
const { hashPassword, comparePassword } = require("../utils/hash");
const { success, error } = require("../utils/responses");

async function register(req, res) {
  console.log("[DEBUG] register: body", req.body);
  try {
    const { fullName, contact, password } = req.body;
    if (!fullName || !contact || !password) return error(res, 400, "faltan campos");
    const exists = await findUserByContact(contact);
    if (exists) return error(res, 400, "contacto ya registrado");
    const hash = await hashPassword(password);
    const user = await createUser(fullName, contact, hash);
    return success(res, { user });
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

async function login(req, res) {
  try {
    const { contact, password } = req.body;
    if (!contact || !password) return error(res, 400, "faltan campos");
    const user = await findUserByContact(contact);
    if (!user) return error(res, 401, "credenciales invalidas");
    const ok = await comparePassword(password, user.password_hash);
    if (!ok) return error(res, 401, "credenciales invalidas");
    delete user.password_hash;
    return success(res, { user });
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

module.exports = { register, login };
