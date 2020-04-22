package com.calcprojects.constructorbuddy.model.units

import androidx.room.TypeConverter
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Form

enum class Unit(val nameRes: Int, val distance: String, val weight: String, val density: String) {

    METRIC(R.string.metric, "cm", "kg", "g/cm3"),
    IMPERIAL(R.string.imperial, "inch", "pound", "lb/in3");
}

class UnitTypeConverter {

    @TypeConverter
    fun getUnitByNameRes(res: Int): Unit? = Unit.values().find { u -> u.nameRes == res }

    @TypeConverter
    fun getUnitName(unit: Unit): Int = unit.nameRes

/*    @TypeConverter
    fun getUnitByDistance(dis: String): Unit? = Unit.values().find { u -> u.distance == dis }

    @TypeConverter
    fun getUnitDistance(unit: Unit): String = unit.distance

    @TypeConverter
    fun getUnitByWeight(weight: String): Unit? = Unit.values().find { u -> u.weight == weight }

    @TypeConverter
    fun getUnitWeight(unit: Unit): String = unit.weight

    @TypeConverter
    fun getUnitByDensity(density: String): Unit? = Unit.values().find { u -> u.density == density }

    @TypeConverter
    fun getUnitDensity(unit: Unit): String = unit.density*/

}