package com.calcprojects.constructorbuddy.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.calcprojects.constructorbuddy.data.api_currency.ApiCurrency
import com.calcprojects.constructorbuddy.data.db.ModelDao
import com.calcprojects.constructorbuddy.data.firebase.*
import com.calcprojects.constructorbuddy.data.firebase.FireStoreApi.getPriceFromSnapshot
import com.calcprojects.constructorbuddy.data.firebase.FireStoreApi.getRatesFromSnapshot
import com.calcprojects.constructorbuddy.data.firebase.FireStoreApi.getTime
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price
import com.calcprojects.constructorbuddy.model.price.getMapFromRates
import com.calcprojects.constructorbuddy.model.units.Unit
import com.calcprojects.constructorbuddy.model.units.fromKGToPound
import com.calcprojects.constructorbuddy.ui.LOG_EXCEPTION
import java.util.*
import kotlin.collections.HashMap


private const val UPDATE_INTERVAL = 60 * 60 * 2

@Suppress("UNCHECKED_CAST")
class Repository(
    private val context: Context,
    private val api: ApiCurrency,
    private val modelDao: ModelDao
) : AppRepository {

    override suspend fun getMaterialPricedWithServerData(
        substance: Substance,
        currencyTo: Currency,
        metric: Boolean,
        priceManually: Price?
    ): Material {
        val price = priceManually ?: run {
            val snapShot = FireStoreApi.getPricesFromFireStore()
            snapShot?.let { getPriceFromSnapshot(it, substance.name) }
        }
        Log.d(LOG_EXCEPTION, "price in getMat... before: $price")

        price?.apply {
            val newPriceValue =
                getRatesMap()?.let {
                    Log.d(LOG_EXCEPTION, "getRatesMap AMD: ${it["AMD"]}")

                    getConvertedPriceValue(value, base, currencyTo, metric, it)
                }
            newPriceValue?.let {
                value = it
                base = currencyTo
            }
        }
        Log.d(LOG_EXCEPTION, "price in getMat... after: $price")

        return Material(substance, price)
    }

    private suspend fun getRatesMap(): HashMap<String, Double>? {

        val nowDate = Calendar.getInstance().time
        val docSnapshot = FireStoreApi.getRatesFromFireStore()
        Log.d(LOG_EXCEPTION, "HAS NETWORK: ${hasNetwork(context)}")

        return if (hasNetwork(context)) {  //HAS NETWORK
            docSnapshot?.let { snapshot ->

                val latestUpdateTime = snapshot.getTime()
                val duration = latestUpdateTime?.let { duration(it, nowDate) }
                Log.d(LOG_EXCEPTION, "duration - INTERVAL: ${duration!! - UPDATE_INTERVAL}")

                if (duration != null && duration < UPDATE_INTERVAL) // STILL NEW RATES IN FB SERVER
                {
                    val i = getRatesFromSnapshot(snapshot)
                    Log.d(LOG_EXCEPTION, "getRatesFromSnapshot : $i")
                    i
                } else {  //NEED TO BE UPDATED
                    try {
                        val response = api.getRatesAsync().await()
                        val base = response.response.base
                        val rates = response.response.rates
                        Log.d(LOG_EXCEPTION, "base from API: ${base}")
                        Log.d(LOG_EXCEPTION, "rates from API: ${rates}")

                        FireStoreApi.writeRatesIntoFireStore(rates, base)
                        getMapFromRates(rates)
                    } catch (ex: Exception) { //PROBLEM WITH API SERVER
                        getRatesFromSnapshot(snapshot) //GET FROM FB OLD RATES
                    }
                }
            }
        } else  //NO Network - GET FROM CACHE
            docSnapshot?.let {
                Log.d(LOG_EXCEPTION, "FROM CACHE CASE")
                getRatesFromSnapshot(it)
            }
    }

    private fun getConvertedPriceValue(
        priceValueFrom: Double,
        currencyFrom: Currency,
        currencyTo: Currency,
        metric: Boolean,
        ratesMap: HashMap<String, Double>
    ): Double? {

        //BASE UNIT FROM SERVER IS KG, SO WE NEED TO CONVERT INTO POUND IF METRIC IS FALSE
        val rateFrom = ratesMap[currencyFrom.name]
        val rateTo = ratesMap[currencyTo.name]
        val valuePerKg = rateFrom?.let { from -> rateTo?.let { to -> priceValueFrom * to / from } }
        return valuePerKg?.let { if (metric) it else fromKGToPound(it) }
    }

    override suspend fun saveModel(model: Model) = modelDao.add(model)

    override suspend fun deleteModels(ids: List<Int>) = ids.forEach { modelDao.remove(it) }

    override fun getModel(id: Int): LiveData<Model?> = modelDao.get(id)

    override fun getAllModels(): LiveData<List<Model>?> = modelDao.getAll()

}