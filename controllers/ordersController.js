const { createOrder } = require("../models/orderModel");
const { clearCart } = require("../models/cartModel");
const { success, error } = require("../utils/responses");

async function place(req, res) {
  try {
    const { userId, address, payment, items } = req.body;
    if (!userId || !items || !items.length) return error(res, 400, "faltan campos");
    const total = items.reduce((s, i) => s + Number(i.qty) * Number(i.unitPrice), 0);
    const result = await createOrder(userId, total, address || "", payment || "unknown", items);
    await clearCart(userId);
    return success(res, { orderId: result.orderId });
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

module.exports = { place };
