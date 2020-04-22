package com.calcprojects.constructorbuddy.model.figures

import com.calcprojects.constructorbuddy.model.price.Price

data class Material(val substance: Substance, val price: Price? = null)