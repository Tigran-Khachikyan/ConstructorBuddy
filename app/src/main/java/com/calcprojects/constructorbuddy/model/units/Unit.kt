package com.calcprojects.constructorbuddy.model.units

import com.calcprojects.constructorbuddy.R

enum class Unit(val nameRes: Int, val distance: String, val weight: String, val density: String) {

    METRIC(R.string.metric, "cm", "kg", "g/cm3"),
    IMPERIAL(R.string.imperial, "inch", "pound", "lb/in3");
}