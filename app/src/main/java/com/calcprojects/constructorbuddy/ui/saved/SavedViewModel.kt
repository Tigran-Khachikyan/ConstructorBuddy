package com.calcprojects.constructorbuddy.ui.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calcprojects.constructorbuddy.data.Repository
import com.calcprojects.constructorbuddy.data.api_currency.ApiCurrency
import com.calcprojects.constructorbuddy.data.api_currency.RetrofitService
import com.calcprojects.constructorbuddy.data.db.Database
import com.calcprojects.constructorbuddy.model.Model

class SavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: Repository by lazy {
        Repository(
            application,
            RetrofitService.createService(ApiCurrency::class.java),
            Database(application).getModelDao()
        )
    }

    fun getSavedModels(): LiveData<List<Model>> {
        return repo.getAllModels()
    }

}