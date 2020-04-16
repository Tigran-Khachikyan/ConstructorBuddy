package com.calcprojects.constructorbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    companion object {
        fun setState(newState: ActivityViewStates) {
            state.value = newState
        }

        private val state = MutableLiveData<ActivityViewStates>()
    }


    fun getState(): LiveData<ActivityViewStates> {
        if (state.value == null)
            state.value = ActivityViewStates.DEFAULT_SHOW_ALL
        return state
    }
}