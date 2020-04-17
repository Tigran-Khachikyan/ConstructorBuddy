package com.calcprojects.constructorbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calcprojects.constructorbuddy.model.StateUIActivity
import kotlinx.coroutines.delay

class MainViewModel : ViewModel() {

    companion object {

        fun setState(newState: StateUIActivity) {
            state.value = newState
        }

        private val state = MutableLiveData<StateUIActivity>()
    }


    fun getState(): LiveData<StateUIActivity> {
        if (state.value == null)
            state.value = StateUIActivity()

        return state
    }
}