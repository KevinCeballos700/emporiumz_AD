const { getAllProducts, getProductBySku } = require("../models/productModel");
const { success, error } = require("../utils/responses");

async function list(req, res) {
  try {
    const rows = await getAllProducts();
    return success(res, rows);
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

async function detail(req, res) {
  try {
    const sku = req.params.sku;
    const p = await getProductBySku(sku);
    if (!p) return error(res, 404, "no encontrado");
    return success(res, p);
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

module.exports = { list, detail };
