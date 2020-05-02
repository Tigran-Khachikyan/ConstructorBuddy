package com.calcprojects.constructorbuddy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    companion object {

        fun showBottomActionView(show: Boolean, withAnimation: Boolean = false) {

            state.value = Pair(show, withAnimation)
        }

        private val state = MutableLiveData<Pair<Boolean, Boolean>>()
    }

    fun getState(): LiveData<Pair<Boolean, Boolean>> {
        if (state.value == null)
            state.value = Pair(first = true, second = false)
        return state
    }
}