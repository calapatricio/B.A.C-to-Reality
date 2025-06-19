package com.cala.bactoreality

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bebidas")
data class Bebida(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val volumenML: Int,
    val graduacionAlcohol: Double,
    val proporcionAlcohol: Double
)
