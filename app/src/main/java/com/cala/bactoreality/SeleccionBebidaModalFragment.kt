package com.cala.bactoreality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SeleccionBebidaModalFragment (
    private val listener: OnBebidaSeleccionadaListener, private val bebidaViewModel: BebidaViewModel): BottomSheetDialogFragment() {
        interface OnBebidaSeleccionadaListener {
            fun onBebidaSeleccionada(bebida: Bebida)
            fun onCrearNuevaBebida()
        }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BebidasAdapter
    private var bebidasList = listOf<Bebida>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.seleccion_bebida_modal_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerBebidasAlcoholicasBD)
        val botonCrear = view.findViewById<Button>(R.id.buttonCrearBebida)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BebidasAdapter(bebidasList)
        recyclerView.adapter = adapter

        loadBebidas()

        botonCrear.setOnClickListener {
            listener.onCrearNuevaBebida()
            dismiss()
        }

        adapter.setOnItemClickListener {
            bebida -> listener.onBebidaSeleccionada(bebida)
            dismiss()
        }

        adapter.setOnDeleteClickListener {
            bebida -> mostrarDialogoConfirmacion(bebida)
        }

        return view
    }

    private fun loadBebidas() {
        bebidaViewModel.bebidas.observe(viewLifecycleOwner) {
            lista -> bebidasList = lista
            adapter.actualizarLista(bebidasList)
        }
    }

    private fun mostrarDialogoConfirmacion(bebida: Bebida) {
        val context = requireContext()
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("¿Eliminar bebida?")
            .setMessage("¿Estás seguro de que quieres eliminar \"${bebida.nombre}\"?")
            .setPositiveButton("Eliminar") {_, _ ->
                bebidaViewModel.deleteBebida(bebida)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}