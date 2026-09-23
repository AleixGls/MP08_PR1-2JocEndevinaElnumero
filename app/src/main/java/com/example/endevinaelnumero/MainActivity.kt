package com.example.endevinaelnumero

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import android.widget.ScrollView

class MainActivity : AppCompatActivity() {

    private var numeroSecreto = 0
    private var intentos = 0
    private lateinit var textViewIntentos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonProbar = findViewById<Button>(R.id.buttonProbar)
        val editTextNumero = findViewById<EditText>(R.id.editTextNumero)
        editTextNumero.setOnEditorActionListener { _, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER &&
                        event.action == android.view.KeyEvent.ACTION_DOWN)
            ) {
                buttonProbar.performClick()
                true
            } else {
                false
            }
        }
        val textViewHistorial = findViewById<TextView>(R.id.textViewHistorial)
        val scrollViewHistorial = findViewById<ScrollView>(R.id.scrollViewHistorial)

        textViewIntentos = findViewById(R.id.textViewIntentos)

        nuevaPartida()

        buttonProbar.setOnClickListener {

            val texto = editTextNumero.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(
                    this,
                    "Introdueix un número",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            val numeroUsuario = texto.toInt()

            intentos++
            textViewIntentos.text = "$intentos"

            editTextNumero.text.clear()

            if (numeroUsuario < numeroSecreto) {
                bajarHistorial(
                    textViewHistorial,
                    scrollViewHistorial,
                    "INTENT $intentos: El número secret és major que $numeroUsuario"
                )

            } else if (numeroUsuario > numeroSecreto) {
                bajarHistorial(
                    textViewHistorial,
                    scrollViewHistorial,
                    "INTENT $intentos: El número secret és menor que $numeroUsuario"
                )

            } else {
                AlertDialog.Builder(this)
                    .setTitle("Enhorabona!")
                    .setMessage("Has encertat el número secret ($numeroSecreto) en $intentos intents!")
                    .setPositiveButton("Nova partida") { _, _ ->
                        textViewHistorial.text = ""
                        nuevaPartida()
                    }.show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun nuevaPartida() {
        numeroSecreto = (1..100).random()
        intentos = 0
        textViewIntentos.text = "0"
    }

    private fun bajarHistorial(
        textViewHistorial: TextView,
        scrollViewHistorial: ScrollView,
        texto: String
    ) {
        textViewHistorial.append("$texto\n")

        scrollViewHistorial.post {
            scrollViewHistorial.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}