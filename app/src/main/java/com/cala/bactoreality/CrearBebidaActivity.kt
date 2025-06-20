package com.cala.bactoreality

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class CrearBebidaActivity : AppCompatActivity() {

    private lateinit var editNombreBebida: EditText
    private lateinit var editVolumen: EditText
    private lateinit var editGraduacionAlcohol:  EditText
    private lateinit var editProporcionAlcohol: EditText
    private lateinit var buttonCrearBebida: Button

    private lateinit var bebidaViewModel: BebidaViewModel

    override fun onCreate(savedInstaceState: Bundle?) {
        super.onCreate(savedInstaceState)
        setContentView(R.layout.crear_bebida)

        editNombreBebida = findViewById(R.id.editNombreBebida)
        editVolumen = findViewById(R.id.editVolumen)
        editGraduacionAlcohol = findViewById(R.id.editGraduacionAlcohol)
        editProporcionAlcohol = findViewById(R.id.editProporcionAlcohol)
        buttonCrearBebida = findViewById(R.id.buttonCrearBebida)

        editNombreBebida.setText("")
        editVolumen.setText("")
        editGraduacionAlcohol.setText("")
        editProporcionAlcohol.setText("")

        val bebidaDao = AppDatabase.getInstance(application).bebidaDao()
        val repository = BebidaRepository(bebidaDao)
        val viewModelFactory = BebidaViewModelFactory(repository)
        bebidaViewModel = ViewModelProvider(this, viewModelFactory).get(BebidaViewModel::class.java)

        buttonCrearBebida.setOnClickListener {
            val nombre = editNombreBebida.text.toString().trim()
            val volumen = editVolumen.text.toString().toIntOrNull()
            val graduacion = editGraduacionAlcohol.text.toString().toDoubleOrNull()
            val proporcion = editProporcionAlcohol.text.toString().toDoubleOrNull()

            if (nombre.isEmpty() || volumen == null || graduacion == null || proporcion == null) {
                Toast.makeText(this, "Por favor, rellena todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (volumen <= 0) {
                Toast.makeText(this, "El volumen debe de ser mayor que 0 ml", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (graduacion < 0 || graduacion > 100) {
                Toast.makeText(this, "La graduación debe de estar entre 0% y 100%", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (proporcion < 0 || proporcion > 1) {
                Toast.makeText(this, "La proporción de alcohol debe estar entre 0% y 100%", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bebida = Bebida(
                nombre = nombre,
                volumenML = volumen,
                graduacionAlcohol = graduacion,
                proporcionAlcohol = proporcion
            )

            bebidaViewModel.insertBebida(bebida)
            Toast.makeText(this, "Bebida creada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}