const express = require("express");
const router = express.Router();
const { place } = require("../controllers/ordersController");

router.post("/", place);

module.exports = router;
