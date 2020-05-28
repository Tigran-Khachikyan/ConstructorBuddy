package com.calcprojects.constructorbuddy.ui

import android.app.Activity
import android.content.Intent
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import java.lang.StringBuilder

interface SendModel {

    val hostActivity: Activity?

    fun shareModels(models: List<Model>) {
        hostActivity?.let {

            val modelsInfoToShare = models.map { m -> m.getResultToSend(it) }
            val strBuilder = StringBuilder()
            modelsInfoToShare.forEach { info -> strBuilder.append(info) }
            val textToSend = strBuilder.toString()
            createSendingIntent(it as MainActivity, textToSend)
        }
    }

    private fun createSendingIntent(activity: Activity, text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent =
            Intent.createChooser(intent, activity.resources.getString(R.string.titleSend))
        activity.startActivity(shareIntent)
    }
}