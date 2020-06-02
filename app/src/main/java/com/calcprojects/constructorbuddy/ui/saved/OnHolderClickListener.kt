package com.calcprojects.constructorbuddy.ui.saved

import com.calcprojects.constructorbuddy.model.Model

interface OnHolderClickListener {

    fun onHolderClick(model:Model)
    fun onHolderLongClick(model: Model): Boolean
}