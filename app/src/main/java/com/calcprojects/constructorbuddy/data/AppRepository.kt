package com.calcprojects.constructorbuddy.data

import androidx.lifecycle.LiveData
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.price.Currency

interface AppRepository {
    suspend fun saveModel(model: Model)
    suspend fun deleteModel(id: Int)
    fun getModel(id: Int): Model
    fun getAllModels(): LiveData<List<Model>>
    suspend fun getPricedMaterial(substance: Substance, currency: Currency): Material
}