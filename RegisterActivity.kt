package com.emporiumz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emporiumz.app.models.RegisterRequest
import com.emporiumz.app.utils.Prefs
import com.emporiumz.databinding.ActivityRegisterBinding
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefs: Prefs
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)

        binding.btnRegister.setOnClickListener { onRegisterClicked() }
        binding.btnBackLogin.setOnClickListener { finish() }
    }

    private fun onRegisterClicked() {
        val name = binding.etFullName.text.toString().trim()
        val contact = binding.etRegContact.text.toString().trim()
        val pass = binding.etRegPassword.text.toString()
        if (name.isEmpty() || contact.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        doRegister(name, contact, pass)
    }

    @SuppressLint("SetTextI18n")
    private fun doRegister(name: String, contact: String, pass: String) {
        binding.btnRegister.isEnabled = false
        binding.btnRegister.text = "Registrando..."
        scope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    ApiClient.apiService.register(RegisterRequest(name, contact, pass))
                }
                if (resp.isSuccessful) {
                    val user = resp.body()?.user
                    if (user != null) {
                        prefs.userId = user.id
                        prefs.userName = user.full_name ?: ""
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registro inválido", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val msg = resp.errorBody()?.string() ?: "Registro fallido"
                    Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.text = "Continuar"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}