package com.cala.bactoreality

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var buttonAgregarBebida: FloatingActionButton
    private lateinit var recyclerBebidasAlcoholicas: RecyclerView
    private lateinit var textSinBebidas: TextView
    private lateinit var buttonCalcular: Button
    private lateinit var editHorasPrimeraBebida : EditText
    private lateinit var editMinutosPrimeraBebida : EditText
    private lateinit var resultGramosPorLitro : TextView
    private lateinit var resultMiligramosPorLitroDeAire: TextView
    private lateinit var resultBAC : TextView
    private lateinit var bebidaViewModel: BebidaViewModel
    private lateinit var adapter: BebidasAdapter
    private val listaBebidas = mutableListOf<Bebida>()
    private val gramosPorDefecto = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAgregarBebida = findViewById(R.id.buttonAgregarBebida)
        recyclerBebidasAlcoholicas = findViewById(R.id.recyclerBebidasAlcoholicas)
        textSinBebidas = findViewById(R.id.textSinBebidas)
        buttonCalcular = findViewById(R.id.buttonCalcular)
        editHorasPrimeraBebida = findViewById(R.id.editHorasPrimeraBebida)
        editMinutosPrimeraBebida = findViewById(R.id.editMinutosPrimeraBebida)
        resultGramosPorLitro = findViewById(R.id.resultGramosPorLitro)
        resultMiligramosPorLitroDeAire = findViewById(R.id.resultMiligramosPorLitroDeAire)
        resultBAC = findViewById(R.id.resultBAC)

        resultGramosPorLitro.text = String.format(Locale.US, "%.2f g/L", gramosPorDefecto)
        resultMiligramosPorLitroDeAire.text = String.format(Locale.US, "%.2f mg/L", gramosPorDefecto)
        resultBAC.text = String.format(Locale.US, "%.3f %%", gramosPorDefecto)


        adapter = BebidasAdapter(listaBebidas)
        recyclerBebidasAlcoholicas.layoutManager = LinearLayoutManager(this)
        recyclerBebidasAlcoholicas.adapter = adapter

        val bebidaDao = AppDatabase.getInstance(application).bebidaDao()
        val repository = BebidaRepository(bebidaDao)
        val viewModelFactory = BebidaViewModelFactory(repository)
        bebidaViewModel = ViewModelProvider(this, viewModelFactory).get(BebidaViewModel::class.java)

        val listener = object : SeleccionBebidaModalFragment.OnBebidaSeleccionadaListener {
            override fun onBebidaSeleccionada(bebida: Bebida) {
                listaBebidas.add(bebida)
                adapter.actualizarLista(listaBebidas)
                actualizarRecyclerView()
            }

            override fun onCrearNuevaBebida() {
                val intent = Intent(this@MainActivity, CrearBebidaActivity::class.java)
                startActivity(intent)
            }
        }


        buttonAgregarBebida.setOnClickListener {
            val modalFragment = SeleccionBebidaModalFragment(listener, bebidaViewModel)
            modalFragment.show(supportFragmentManager, "SeleccionBebidaModalFragment")
        }

        adapter.setOnDeleteClickListener {
            bebida -> listaBebidas.remove(bebida)
            adapter.actualizarLista(listaBebidas)
            actualizarRecyclerView()
        }

        buttonCalcular.setOnClickListener {
            val peso = 59.0
            val factorGenero = 0.68 //0.55 en mujeres
            val tasaEliminacion = 0.1 //Entre 0.1 y 0.2

            val horasTexto = editHorasPrimeraBebida.text.toString().filter { it.isDigit() }
            val minutosTexto = editMinutosPrimeraBebida.text.toString().filter { it.isDigit() }

            val horas = horasTexto.toIntOrNull() ?: 0
            val minutos = minutosTexto.toIntOrNull() ?:0
            val tiempoHoras = horas.toDouble() + (minutos.toDouble() / 60)

            var gramosTotales = 0.0
            val gramosPorML = 0.789
            val graduacionDecimal = 0.01
            for (bebida in listaBebidas) {
                val gramos = bebida.volumenML * (bebida.graduacionAlcohol * graduacionDecimal) * bebida.proporcionAlcohol * gramosPorML
                gramosTotales += gramos
            }

            val factorAlcoholEnAireExpirado = 2
            val coeficienteBAC = 0.1

            val gramosPorLitroInicial = (gramosTotales / (peso * factorGenero))
            val gramosPorLitroFinal = (gramosPorLitroInicial - (tasaEliminacion * tiempoHoras)).coerceAtLeast(0.0)
            val mgPorLitro = gramosPorLitroFinal * factorAlcoholEnAireExpirado
            val bac = gramosPorLitroFinal * coeficienteBAC

            resultGramosPorLitro.text = String.format(Locale.US, "%.2f g/L", gramosPorLitroFinal)
            resultMiligramosPorLitroDeAire.text = String.format(Locale.US, "%.2f mg/L", mgPorLitro)
            resultBAC.text = String.format(Locale.US, "%.3f %%", bac)

        }
    }

    private fun actualizarRecyclerView() {
        if(listaBebidas.isEmpty()) {
            recyclerBebidasAlcoholicas.visibility = View.GONE
            textSinBebidas.visibility = View.VISIBLE
        } else {
            recyclerBebidasAlcoholicas.visibility = View.VISIBLE
            textSinBebidas.visibility = View.GONE
        }
    }
}