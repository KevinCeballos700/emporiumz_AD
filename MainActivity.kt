package com.emporiumz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.emporiumz.app.adapters.ProductAdapter
import com.emporiumz.app.models.CartRequest
import com.emporiumz.app.utils.Prefs
import com.emporiumz.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ProductAdapter.OnAddToCartListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: Prefs
    private lateinit var adapter: ProductAdapter

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)

        adapter = ProductAdapter(this)
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter

        binding.btnOpenWeb.setOnClickListener {
            binding.webView.visibility = View.VISIBLE
            binding.webView.webViewClient = WebViewClient()
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl("http://10.0.2.2:4000/")
        }

        binding.fabCart.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        loadProducts()
    }

    private fun loadProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resp = ApiClient.apiService.getProducts()
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        val list = resp.body() ?: emptyList()
                        adapter.submitList(list)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error cargando productos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e:Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onAddToCart(sku: String) {
        val userId = prefs.userId
        if (userId == 0) { Toast.makeText(this, "Inicia sesión primero", Toast.LENGTH_SHORT).show(); return }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resp = ApiClient.apiService.addToCart(CartRequest(userId, sku, 1))
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) Toast.makeText(
                        this@MainActivity,
                        "Agregado al carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                    else Toast.makeText(this@MainActivity, "Error al agregar", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e:Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}