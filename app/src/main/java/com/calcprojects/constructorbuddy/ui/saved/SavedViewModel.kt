package com.calcprojects.constructorbuddy.ui.saved

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.calcprojects.constructorbuddy.data.Repository
import com.calcprojects.constructorbuddy.data.api_currency.ApiCurrency
import com.calcprojects.constructorbuddy.data.api_currency.RetrofitService
import com.calcprojects.constructorbuddy.data.db.Database
import com.calcprojects.constructorbuddy.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class SavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: Repository by lazy {
        Repository(
            application,
            RetrofitService.createService(ApiCurrency::class.java),
            Database(application).getModelDao()
        )
    }

    fun getSavedModels(): LiveData<List<Model>> = repo.getAllModels()
    fun removeModel(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.deleteModels(ids)
        }
    }

}