package com.emporiumz

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emporiumz.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    @Deprecated("Deprecated in Java")
    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        super.onCreate(savedInstanceState)
        // Inflate layout via ViewBinding (asegúrate activity_checkout.xml existe)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner de métodos de pago (local, sin dependencias externas)
        val payments = listOf("Tarjeta", "PayPal", "Transferencia")
        binding.spinnerPayment.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, payments)

        // Listeners básicos
        binding.btnLoadCart.setOnClickListener { loadCartDummy() }
        binding.btnConfirm.setOnClickListener { confirmOrderDummy() }
    }

    // Carga de carrito mínima para probar la UI (rellena checkoutItems con filas de texto)
    @SuppressLint("SetTextI18n")
    private fun loadCartDummy() {
        // Limpia contenedor
        binding.checkoutItems.removeAllViews()

        // Datos de ejemplo — reemplaza aquí por llamada al backend cuando todo compile
        val sample = listOf(
            Triple("Camisa Polo", 1, 35000),
            Triple("IPhone 12", 1, 199900),
            Triple("Adidas Copa 3", 2, 280000)
        )

        var total = 0
        for ((name, qty, price) in sample) {
            val tv = TextView(this).apply {
                text = "$name x$qty — $${(qty * price).toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")}"
                setPadding(8, 8, 8, 8)
            }
            binding.checkoutItems.addView(tv)
            total += qty * price
        }

        binding.checkoutTotal.text = "$${total.toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")}"
        Toast.makeText(this, "Carrito cargado (datos de ejemplo)", Toast.LENGTH_SHORT).show()
    }

    // Confirmación mínima (solo UI) — reemplace por integración real a API cuando esté listo
    @SuppressLint("SetTextI18n")
    private fun confirmOrderDummy() {
        val address = binding.etAddress.text.toString().trim()
        if (address.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa la dirección de envío", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = (binding.spinnerPayment.selectedItem ?: "desconocido").toString()
        // Aquí iría la llamada al backend para crear la orden.
        // Se deja como Toast para mantener compilación y permitir pruebas UI rápidas.
        Toast.makeText(this, "Orden simulada: $paymentMethod — Enviando a: $address", Toast.LENGTH_LONG).show()

        // Opcional: limpiar UI tras "confirmación"
        binding.checkoutItems.removeAllViews()
        binding.checkoutTotal.text = "$0"
    }
}