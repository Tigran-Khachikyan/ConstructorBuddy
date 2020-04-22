package com.calcprojects.constructorbuddy.model.price

import com.calcprojects.constructorbuddy.model.units.Unit

data class Price(
    var base: Currency,
    var value: Double,
    var unit: String
)
