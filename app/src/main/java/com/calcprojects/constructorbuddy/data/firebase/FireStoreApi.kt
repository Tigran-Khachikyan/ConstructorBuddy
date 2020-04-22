package com.calcprojects.constructorbuddy.data.firebase

import com.calcprojects.constructorbuddy.data.api_currency.Rates
import com.calcprojects.constructorbuddy.data.firebase.FBCollection.*
import com.calcprojects.constructorbuddy.model.price.getMapFromRates
import com.google.firebase.firestore.*
import java.io.Serializable
import java.util.*

private const val CACHE = "Cached Rates"
const val DATE = "Short Date"
const val BASE = "Base"
const val DATE_TIME = "Long Date"
const val RATES = "Rates"
const val PRICING = "Pricing"
const val NO_NETWORK = 123
const val API_SOURCE_PROBLEM = 255
const val OK = 777

object FireStoreApi {

    private val fireStoreDb by lazy { FirebaseFirestore.getInstance() }

    fun insertRatesIntoFireStore(rates: Rates, base: String) {

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

    fun getFromCache(): DocumentSnapshot? {
        return fireStoreDb.collection(CURRENCY_LATEST.name).document(CACHE).get(Source.CACHE).result
    }

    fun getMaterialPrices(): DocumentSnapshot? {
        return fireStoreDb.collection(MATERIALS.name).document(PRICING).get().result
    }

    private fun writeToFireStore(docId: String, map: HashMap<String, Serializable>) {
        fireStoreDb.collection(CURRENCY_LATEST.name)
            .document(docId)
            .set(map)
            .addOnCompleteListener {}
            .addOnFailureListener {}
    }
}