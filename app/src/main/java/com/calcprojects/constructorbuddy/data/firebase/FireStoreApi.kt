@file:Suppress("UNCHECKED_CAST")

package com.calcprojects.constructorbuddy.data.firebase

import android.util.Log
import com.calcprojects.constructorbuddy.data.api_currency.Rates
import com.calcprojects.constructorbuddy.data.firebase.FBCollection.*
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price
import com.calcprojects.constructorbuddy.model.price.getMapFromRates
import com.calcprojects.constructorbuddy.model.units.Unit
import com.calcprojects.constructorbuddy.ui.LOG_EXCEPTION
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.lang.Exception
import java.util.*

private const val CACHE = "Cached Rates"
private const val CACHE_SIZE = 1024 * 1024L
const val DATE = "Short Date"
const val BASE = "Base"
const val DATE_TIME = "Long Date"
const val RATES = "Rates"
const val PRICING = "Pricing"
private const val BASE_CURRENCY = "base"
private const val UNIT = "unit"

object FireStoreApi {

    private val fireStoreDb by lazy {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(CACHE_SIZE)
                .build()
        }
    }

    suspend fun writeRatesIntoFireStore(rates: Rates, base: String) {

        val nowDate = Calendar.getInstance().time
        val now = nowDate.getStringTimeStampWithDate()
        val nowShort = now.split(' ')[0]

        val resultMap = getMapFromRates(rates)?.let {
            hashMapOf(
                DATE to nowShort,
                DATE_TIME to now,
                BASE to base,
                RATES to it
            )
        }
        resultMap?.let {
            insertRatesMapIntoFireBase(now, it)
            insertRatesMapIntoFireBase(CACHE, it)
        }
    }

    suspend fun getRatesFromFireStore(): DocumentSnapshot? {
        return try {
            fireStoreDb.collection(CURRENCY_LATEST.name).document(CACHE).get().await()
        } catch (ex: Exception) {
            Log.d(LOG_EXCEPTION, "ERROR message getRatesFromFireStore: ${ex.message}")
            null
        }
    }

    suspend fun getPricesFromFireStore(): DocumentSnapshot? {
        return try {
            fireStoreDb.collection(MATERIALS.name).document(PRICING).get().await()
        } catch (ex: Exception) {
            Log.d(LOG_EXCEPTION, "ERROR message getPricesFromFireStore: ${ex.message}")
            null
        }
    }

    private suspend fun insertRatesMapIntoFireBase(
        documentId: String, ratesMap: HashMap<String, Serializable>
    ): Boolean {
        return try {
            fireStoreDb.collection(CURRENCY_LATEST.name)
                .document(documentId)
                .set(ratesMap)
                .await()
            true
        } catch (ex: Exception) {
            Log.d(LOG_EXCEPTION, "ERROR message insertRatesMapIntoFireBase: ${ex.message}")
            false
        }
    }

    fun getRatesFromSnapshot(snapshot: DocumentSnapshot): HashMap<String, Double>? {
        return try {
            snapshot.get(RATES) as HashMap<String, Double>
        } catch (ex: Exception) {
            Log.d(LOG_EXCEPTION, "ERROR message getRatesFromSnapshot: ${ex.message}")
            null
        }
    }

    fun DocumentSnapshot.getTime(): Date? {
        val value = this[DATE_TIME] as String
        return value.getDateWithServerTimeStamp()
    }

    fun getPriceFromSnapshot(snapshot: DocumentSnapshot, fieldMaterialName: String): Price? {
        return try {
            val value = try {
                snapshot.get(fieldMaterialName) as Double
            } catch (ex: Exception) {
                (snapshot.get(fieldMaterialName) as Long).toDouble()
            }
            val base = snapshot.get(BASE_CURRENCY) as String
            val baseCurrency = Currency.valueOf(base)

            Price(baseCurrency, value)
        } catch (ex: Exception) {
            Log.d(LOG_EXCEPTION, "ERROR message getPricesFromSnapshot: ${ex.message}")
            null
        }
    }
}