package com.calcprojects.constructorbuddy.data.firebase

import android.util.Log
import com.calcprojects.constructorbuddy.data.api_currency.Rates
import com.calcprojects.constructorbuddy.data.firebase.FBCollection.*
import com.calcprojects.constructorbuddy.model.price.getMapFromRates
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
const val NO_NETWORK = 123
const val API_SOURCE_PROBLEM = 255
const val OK = 777

object FireStoreApi {

    private val fireStoreDb by lazy {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(CACHE_SIZE)
                .build()
        }
    }

    suspend fun insertRatesIntoFireStore(rates: Rates, base: String) {

        val nowDate = Calendar.getInstance().time
        val now = nowDate.getStringTimeStampWithDate()
        val nowShort = now.split(' ')[0]

        getMapFromRates(rates)?.let {
            val resultMap = hashMapOf(
                DATE to nowShort,
                DATE_TIME to now,
                BASE to base,
                RATES to it
            )
            writeToFireStore(now, resultMap)
            writeToFireStore(CACHE, resultMap)
        }
    }

    suspend fun getFromCache(): DocumentSnapshot? {

        Log.d("kasynsdf", "cache size: ${fireStoreDb.firestoreSettings.cacheSizeBytes}")


        return try {
            fireStoreDb.collection(CURRENCY_LATEST.name).document(CACHE).get(Source.CACHE).await()
        } catch (ex: Exception) {
            Log.d("kasynsdf", "message: ${ex.message}")
            null
        }
    }

    suspend fun getMaterialPrices(): DocumentSnapshot? {
        return try {
            fireStoreDb.collection(MATERIALS.name).document(PRICING).get().await()
        } catch (ex: Exception) {
            Log.d("kasynsdf", "message: ${ex.message}")
            null
        }
    }

    private suspend fun writeToFireStore(
        docId: String,
        map: HashMap<String, Serializable>
    ): Boolean {
        return try {
            fireStoreDb.collection(CURRENCY_LATEST.name)
                .document(docId)
                .set(map)
                .await()
            true
        } catch (ex: Exception) {
            false
        }
    }
}