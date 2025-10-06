require("dotenv").config();
const express = require("express");
const path = require("path");
const cors = require("cors");

const app = express();
const PORT = process.env.PORT || 4000;

app.use(cors());
app.use(express.json({ limit: "10mb" }));
app.use(express.urlencoded({ extended: true }));

// Servir public para WebView
app.use(express.static(path.join(__dirname, "public")));

// Rutas API
app.use("/api/auth", require("./src/routes/auth"));
app.use("/api/products", require("./src/routes/products"));
app.use("/api/cart", require("./src/routes/cart"));
app.use("/api/orders", require("./src/routes/orders"));
app.use("/api/inventory", require("./src/routes/inventory"));
app.use("/api/tracking", require("./src/routes/tracking"));

// Alias opcional para compatibilidad
const { register } = require("./src/controllers/authController");
app.post("/api/register", register);

// Ruta raíz -> index.html
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "public", "index.html"));
});

app.get("/health", (req, res) => res.json({ ok: true }));

app.listen(PORT, () => {
  console.log(`Server listening on http://localhost:${PORT}`);
});
