package com.example.checkboxswitch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText
    private lateinit var switchRecordar: Switch
    private lateinit var cbTerminos: CheckBox
    private lateinit var cbComunicaciones: CheckBox
    private lateinit var btnIngresar: Button

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        switchRecordar = findViewById(R.id.switchRecordar)
        cbTerminos = findViewById(R.id.cbTerminos)
        cbComunicaciones = findViewById(R.id.cbComunicaciones)
        btnIngresar = findViewById(R.id.btnIngresar)

        // Si ya existe una contraseña recordada, se precarga el formulario
        cargarPreferenciasGuardadas()

        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbTerminos.isChecked) {
                Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gestión del Switch: guardar o eliminar la contraseña recordada
            if (switchRecordar.isChecked) {
                guardarCredenciales(usuario, password)
                Toast.makeText(this, "Contraseña guardada", Toast.LENGTH_SHORT).show()
            } else {
                eliminarCredenciales()
                Toast.makeText(this, "Contraseña eliminada / no guardada", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(
                this,
                "Comunicaciones comerciales: ${if (cbComunicaciones.isChecked) "Sí" else "No"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun guardarCredenciales(usuario: String, password: String) {
        prefs.edit()
            .putString("usuario", usuario)
            .putString("password", password)
            .putBoolean("recordar", true)
            .apply()
    }

    private fun eliminarCredenciales() {
        prefs.edit()
            .remove("usuario")
            .remove("password")
            .putBoolean("recordar", false)
            .apply()
    }

    private fun cargarPreferenciasGuardadas() {
        val recordar = prefs.getBoolean("recordar", false)
        switchRecordar.isChecked = recordar
        if (recordar) {
            etUsuario.setText(prefs.getString("usuario", ""))
            etPassword.setText(prefs.getString("password", ""))
        }
    }
}
