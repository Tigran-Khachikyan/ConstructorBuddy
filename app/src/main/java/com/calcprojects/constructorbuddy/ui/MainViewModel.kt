package com.calcprojects.constructorbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    companion object {
        fun setState(newState: ParentViewState) {
            state.value = newState
        }

        private val state = MutableLiveData<ParentViewState>()
    }


    fun getState(): LiveData<ParentViewState> {
        if (state.value == null)
            state.value = ParentViewState.DEFAULT_SHOW_ALL
        return state
    }
}