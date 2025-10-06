const { getTracking } = require("../models/trackingModel");
const { success, error } = require("../utils/responses");

async function getTrack(req, res) {
  try {
    const tn = req.params.trackingNumber;
    const t = await getTracking(tn);
    if (!t) return error(res, 404, "no encontrado");
    return success(res, t);
  } catch (err) {
    console.error(err);
    return error(res, 500, err.message);
  }
}

module.exports = { getTrack };
