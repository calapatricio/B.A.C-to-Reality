package com.cala.bactoreality

import androidx.lifecycle.LiveData

class BebidaRepository (private val bebidaDao: BebidaDao) {
    suspend fun insert(bebida: Bebida) {
        bebidaDao.insertBebida(bebida)
    }

    suspend fun delete(bebida: Bebida) {
        bebidaDao.deleteBebida(bebida)
    }

    fun getAll() : LiveData<List<Bebida>> {
        return bebidaDao.getAllBebidas()
    }
}