const { upsertCartItem, getCartByUser } = require("../models/cartModel");
const { success, error } = require("../utils/responses");

async function add(req, res) {
  try {
    const { userId, sku, qty } = req.body;
    if (!userId || !sku || !qty) return error(res, 400, "faltan campos");
    await upsertCartItem(userId, sku, Number(qty));
    return success(res, { ok: true });
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

async function get(req, res) {
  try {
    const userId = Number(req.query.userId);
    if (!userId) return error(res, 400, "userId requerido");
    const items = await getCartByUser(userId);
    return success(res, items);
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

module.exports = { add, get };
