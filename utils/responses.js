function success(res, payload) { return res.json(payload); }
function error(res, status=500, message="error") { return res.status(status).json({ error: message }); }

module.exports = { success, error };
