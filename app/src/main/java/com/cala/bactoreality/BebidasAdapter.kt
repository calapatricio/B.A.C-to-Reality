package com.cala.bactoreality

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BebidasAdapter (private var bebidas: List<Bebida>) : RecyclerView.Adapter<BebidasAdapter.BebidaViewHolder>() {

    private var onItemClickListener: ((Bebida) -> Unit)? = null

    fun setOnItemClickListener(listener: (Bebida) -> Unit) {
        onItemClickListener = listener
    }

    inner class BebidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textNombreBebida)
        val volumenTextView: TextView = itemView.findViewById(R.id.textVolumen)
        val graduacionTextView: TextView = itemView.findViewById(R.id.textGraduacionAlcohol)

        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(bebidas[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BebidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bebida, parent, false)
        return BebidaViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BebidaViewHolder, position: Int) {
        val bebida = bebidas[position]
        holder.nombreTextView.text = bebida.nombre
        holder.volumenTextView.text = "${bebida.volumenML} ml"
        holder.graduacionTextView.text = "${bebida.graduacionAlcohol} %"
    }

    override fun getItemCount(): Int = bebidas.size

    fun actualizarLista(nuevaLista: List<Bebida>) {
        bebidas = nuevaLista
        notifyDataSetChanged()
    }
}