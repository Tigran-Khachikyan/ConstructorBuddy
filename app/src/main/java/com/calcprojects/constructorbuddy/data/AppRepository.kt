package com.calcprojects.constructorbuddy.data

import androidx.lifecycle.LiveData
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price

interface AppRepository {
    suspend fun saveModel(model: Model)
    suspend fun deleteModels(ids: List<Int>)
    fun getModel(id: Int): LiveData<Model>
    fun getAllModels(): LiveData<List<Model>>
    suspend fun getAutoPricedMaterial(substance: Substance, currency: Currency): Material
    suspend fun getManuallyPricedMaterial(sub: Substance, cur: Currency, pr: Price): Material
}