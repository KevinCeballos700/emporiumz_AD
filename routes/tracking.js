const express = require("express");
const router = express.Router();
const { getTrack } = require("../controllers/trackingController");

router.get("/:trackingNumber", getTrack);

module.exports = router;
