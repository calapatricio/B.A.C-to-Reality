package com.cala.bactoreality

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var buttonAgregarBebida: FloatingActionButton
    private lateinit var recyclerBebidasAlcoholicas: RecyclerView
    private lateinit var buttonCalcular: Button
    private lateinit var editHorasPrimeraBebida : EditText
    private lateinit var editMinutosPrimeraBebida : EditText
    private lateinit var bebidaViewModel: BebidaViewModel
    private lateinit var adapter: BebidasAdapter
    private val listaBebidas = mutableListOf<Bebida>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAgregarBebida = findViewById(R.id.buttonAgregarBebida)
        recyclerBebidasAlcoholicas = findViewById(R.id.recyclerBebidasAlcoholicas)
        editHorasPrimeraBebida = findViewById(R.id.editHorasPrimeraBebida)
        editMinutosPrimeraBebida = findViewById(R.id.editMinutosPrimeraBebida)
        buttonCalcular = findViewById(R.id.buttonCalcular)

        adapter = BebidasAdapter(listaBebidas)
        recyclerBebidasAlcoholicas.layoutManager = LinearLayoutManager(this)
        recyclerBebidasAlcoholicas.adapter = adapter

        val bebidaDao = AppDatabase.getInstance(application).bebidaDao()
        val repository = BebidaRepository(bebidaDao)
        val viewModelFactory = BebidaViewModelFactory(repository)
        bebidaViewModel = ViewModelProvider(this, viewModelFactory).get(BebidaViewModel::class.java)

        bebidaViewModel.bebidas.observe(this) {
            lista -> listaBebidas.clear()
            listaBebidas.addAll(lista)
            adapter.actualizarLista(listaBebidas)
        }

        val listener = object : SeleccionBebidaModalFragment.OnBebidaSeleccionadaListener {
            override fun onBebidaSeleccionada(bebida: Bebida) {
                listaBebidas.add(bebida)
                adapter.actualizarLista(listaBebidas)
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

        buttonCalcular.setOnClickListener {
            TODO("Calcular los g/L, mg/L y BAC de cada alcohol individualmente, sumarlo todo y restar 0.1 x ((hora * 60 + minutos) / 60) al total ")
        }
    }
}