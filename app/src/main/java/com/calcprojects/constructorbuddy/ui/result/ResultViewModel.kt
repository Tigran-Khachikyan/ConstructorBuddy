package com.calcprojects.constructorbuddy.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.calcprojects.constructorbuddy.data.Repository
import com.calcprojects.constructorbuddy.data.api_currency.ApiCurrency
import com.calcprojects.constructorbuddy.data.api_currency.RetrofitService
import com.calcprojects.constructorbuddy.data.db.Database
import com.calcprojects.constructorbuddy.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: Repository by lazy {
        Repository(
            application,
            RetrofitService.createService(ApiCurrency::class.java),
            Database(application).getModelDao()
        )
    }

    fun save(model: Model) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.saveModel(model)
        }
    }

    fun getModel(id: Int): LiveData<Model> = repo.getModel(id)


}
