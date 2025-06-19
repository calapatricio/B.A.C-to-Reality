package com.cala.bactoreality

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BebidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBebida(bebida: Bebida)

    @Query("SELECT * FROM bebidas")
    fun getAllBebidas(): LiveData<List<Bebida>>

    @Delete
    suspend fun deleteBebida(bebida: Bebida)
}