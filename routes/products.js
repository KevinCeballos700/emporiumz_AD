const express = require("express");
const router = express.Router();
const { list, detail } = require("../controllers/productsController");

router.get("/", list);
router.get("/:sku", detail);

module.exports = router;
