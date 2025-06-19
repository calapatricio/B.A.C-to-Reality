package com.cala.bactoreality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BebidaViewModel(private val repository: BebidaRepository) : ViewModel() {

    val bebidas: LiveData<List<Bebida>> = repository.getAll()

    fun insertBebida(bebida: Bebida) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(bebida)
        }
    }

    fun deleteBebida(bebida: Bebida) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(bebida)
        }
    }
}