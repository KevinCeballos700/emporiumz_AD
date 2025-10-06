package com.emporiumz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emporiumz.app.models.LoginRequest
import com.emporiumz.app.utils.Prefs
import com.emporiumz.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefs: Prefs
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)

        applyUi()
        bindListeners()
        if (prefs.userId != 0) { startActivity(Intent(this, MainActivity::class.java)); finish() }
    }

    private fun applyUi() {
        // colores y textos ya vienen del XML; opcional: ajustar status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.emporium_purple, theme)
        }
    }

    private fun bindListeners() {
        binding.btnLogin.setOnClickListener { onLoginClicked() }
        binding.tvShowRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        binding.userTab.setOnClickListener { /* manejar cambio de pestaña si necesitas */ }
        binding.adminTab.setOnClickListener { /* manejar admin */ }
    }

    private fun onLoginClicked() {
        val contact = binding.etContact.text.toString().trim()
        val password = binding.etPassword.text.toString()
        if (contact.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        performLogin(contact, password)
    }

    @SuppressLint("SetTextI18n")
    private fun performLogin(contact: String, password: String) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Ingresando..."
        binding.root.alpha = 0.98f
        scope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    ApiClient.apiService.login(LoginRequest(contact, password))
                }
                if (resp.isSuccessful) {
                    val user = resp.body()?.user
                    if (user != null) {
                        prefs.userId = user.id
                        prefs.userName = user.full_name ?: ""
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Respuesta inválida del servidor", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val msg = resp.errorBody()?.string() ?: "Login fallido"
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Ingresar"
                binding.root.alpha = 1f
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}