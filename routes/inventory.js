const express = require("express");
const router = express.Router();
const { list } = require("../controllers/inventoryController");

router.get("/", list);

module.exports = router;
