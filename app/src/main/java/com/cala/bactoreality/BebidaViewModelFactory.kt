package com.cala.bactoreality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BebidaViewModelFactory(private val repository: BebidaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BebidaViewModel::class.java)) {
            return BebidaViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Clase ViewModel desconocida")
    }
}