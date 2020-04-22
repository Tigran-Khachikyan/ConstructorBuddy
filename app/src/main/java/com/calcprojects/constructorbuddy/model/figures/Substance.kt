package com.calcprojects.constructorbuddy.model.figures

import androidx.room.TypeConverter
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.units.Unit


enum class Substance(val nameRes: Int, val imageRes: Int, val density: Double) {
    ALUMINIUM(R.string.aluminium, R.mipmap.mat_aluminium, 2.7),
    SILVER(R.string.silver, R.mipmap.mat_silver, 19.32),
    BRASS(R.string.brass, R.mipmap.mat_brass, 19.32),
    TIN(R.string.tin, R.mipmap.mat_tin, 7.3),
    BRONZE(R.string.bronze, R.mipmap.mat_bronze, 19.32),
    CAST_IRON(R.string.cast_iron, R.mipmap.mat_cast_iron, 19.32),
    PLATINUM(R.string.platinum, R.mipmap.mat_platinum, 19.32),
    CHROME(R.string.chrome, R.mipmap.mat_chrome, 19.32),
    GOLD(R.string.gold, R.mipmap.mat_gold, 19.32);

    val unit = Unit.METRIC.density
}

class SubstanceTypeConverter {

    @TypeConverter
    fun getSubstanceByNameRes(res: Int): Substance? = Substance.values().find { s -> s.nameRes == res }

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