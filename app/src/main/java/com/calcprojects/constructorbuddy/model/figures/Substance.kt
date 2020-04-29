package com.calcprojects.constructorbuddy.model.figures

import androidx.room.TypeConverter
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.units.Unit


enum class Substance(val nameRes: Int, val density: Double) {
    ALUMINIUM(R.string.aluminium, 2.7),
    SILVER(R.string.silver, 19.32),
    BRASS(R.string.brass, 19.32),
    TIN(R.string.tin, 7.3),
    BRONZE(R.string.bronze, 19.32),
    CAST_IRON(R.string.cast_iron, 19.32),
    PLATINUM(R.string.platinum, 19.32),
    CHROME(R.string.chrome, 19.32),
    GOLD(R.string.gold, 19.32);

    val unit = Unit.METRIC.density
}

class SubstanceTypeConverter {

    @TypeConverter
    fun getSubstanceByNameRes(res: Int): Substance? =
        Substance.values().find { s -> s.nameRes == res }

    @TypeConverter
    fun getSubstanceName(substance: Substance): Int = substance.nameRes

    /*  @TypeConverter
      fun getSubstanceByImage(res: Int): Substance? = Substance.values().find { s -> s.imageRes == res }

      @TypeConverter
      fun getSubstanceImage(substance: Substance): Int = substance.imageRes*/

    /*   @TypeConverter
       fun getSubstanceByDensity(res: Int): Substance? = Substance.values().find { s -> s.nameRes == res }

       @TypeConverter
       fun getSubstanceName(substance: Substance): Int = substance.nameRes*/

}