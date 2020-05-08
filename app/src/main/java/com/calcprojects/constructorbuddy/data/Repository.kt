package com.calcprojects.constructorbuddy.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.calcprojects.constructorbuddy.data.api_currency.ApiCurrency
import com.calcprojects.constructorbuddy.data.db.ModelDao
import com.calcprojects.constructorbuddy.data.firebase.*
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price
import com.calcprojects.constructorbuddy.model.price.getMapFromRates
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.HashMap


private const val UPDATE_INTERVAL = 60 * 60 * 2
private const val BASE_CURRENCY = "base"
private const val UNIT = "unit"

@Suppress("UNCHECKED_CAST")
class Repository(
    private val context: Context,
    private val api: ApiCurrency,
    private val modelDao: ModelDao
) : AppRepository {

    override suspend fun getAutoPricedMaterial(substance: Substance, currency: Currency): Material {
        Log.d("kasynsdf", "entered in getAutoPricedMaterial")


        val snapShot = FireStoreApi.getMaterialPrices()
        Log.d("kasynsdf", "snapShot: $snapShot")

        val price = snapShot?.let { getPricesFromSnapshot(it, substance.name) }
        Log.d("kasynsdf", "price From SnapShot: $price")

        price?.apply {
            val newValue = getRates()?.let {
                Log.d("kasynsdff", "getRates AMD = ${it["AMD"]}")
                Log.d("kasynsdff", "value: $value")
                Log.d("kasynsdff", "base: $base")
                Log.d("kasynsdff", "currency: $currency")

                getPriceForNewRate(value, base, currency, it)
            }
            newValue?.let {
                value = it
                base = currency
            }
        }
        return Material(substance, price)
    }

    override suspend fun getManuallyPricedMaterial(
        sub: Substance, cur: Currency, pr: Price
    ): Material {

        return Material(Substance.ALUMINIUM, null)
    }

    private suspend fun getRates(): HashMap<String, Double>? {

        Log.d("kasynsdf", "entered getValues()")

        val nowDate = Calendar.getInstance().time
        //Network ok
        return if (hasNetwork(context)) {

            Log.d("kasynsdf", "Has Network")

            val docSnapshot = FireStoreApi.getFromCache()
            Log.d("kasynsdf", "docSnapshot: $docSnapshot")

            docSnapshot?.run {

                val latestDateTime = getTime()
                val duration = latestDateTime?.let { duration(it, nowDate) }
                Log.d("kasynsdf", "duration: $duration")
                Log.d("kasynsdf", "duration > INTERVAL na: ${duration!! - UPDATE_INTERVAL}")
                if (duration != null && duration < UPDATE_INTERVAL)
                    getRatesFromSnapshot(this)
                else {  //catch new rates
                    try {
                        val response = api.getLatestRatesAsync().await()
                        Log.d("kasynsdf", "response: $response")

                        val base = response.response.base
                        val rates = response.response.rates
                        Log.d("kasynsdf", "base: $base")

                        Log.d("kasynsdf", "ratesAPI: $rates")

                        FireStoreApi.insertRatesIntoFireStore(rates, base)
                        getMapFromRates(rates)
                    } catch (ex: Exception) {
                        Log.d("kasynsdf", "API ex message: ${ex.message}")

                        //there is a problem with source, get CACHED from Firestore
                        try {
                            getRatesFromSnapshot(this)
                        } catch (ex: Exception) {
                            //fireStore CACHE error
                            null
                        }
                    }
                }
            }
        } else {  //NO Network - Firestore Cache
            try {
                FireStoreApi.getFromCache()?.let { getRatesFromSnapshot(it) }
            } catch (ex: Exception) {
                null
            }
        }
    }

    private fun getRatesFromSnapshot(snapshot: DocumentSnapshot): HashMap<String, Double> {
        return snapshot.get(RATES) as HashMap<String, Double>
    }

    private fun DocumentSnapshot.getTime(): Date? {
        val value = this[DATE_TIME] as String
        return value.getDateWithServerTimeStamp()
    }

    private fun getPricesFromSnapshot(snapshot: DocumentSnapshot, field: String): Price {
        val value = snapshot.get(field) as Double
        val base = snapshot.get(BASE_CURRENCY) as String
        val baseCurrency = Currency.valueOf(base)
        val unit = snapshot.get(UNIT) as String
        return Price(baseCurrency, value, unit)
    }

    private fun getPriceForNewRate(
        price: Double, curEstimated: Currency, curWanted: Currency, rateMap: HashMap<String, Double>
    ): Double? {
        val rateEstimated = rateMap[curEstimated.name]
        val rateWanted = rateMap[curWanted.name]
        return if (rateEstimated != null && rateWanted != null)
            price * rateWanted / rateEstimated else null
    }

    override suspend fun saveModel(model: Model) = modelDao.add(model)

    override suspend fun deleteModels(ids: List<Int>) = ids.forEach { modelDao.remove(it) }

    override fun getModel(id: Int): LiveData<Model> = modelDao.get(id)

    override fun getAllModels(): LiveData<List<Model>> = modelDao.getAll()

}