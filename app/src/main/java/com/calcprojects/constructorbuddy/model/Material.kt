package com.calcprojects.constructorbuddy.model

import com.calcprojects.constructorbuddy.R


enum class Material(val nameRes: Int, val imageRes: Int, val density: Float) {
    ALUMINIUM (R.string.aluminium, R.mipmap.mat_aluminium, 2.7F),
    SILVER (R.string.silver, R.mipmap.mat_silver, 19.32F),
    BRASS (R.string.brass, R.mipmap.mat_brass, 19.32F),
    TIN (R.string.tin, R.mipmap.mat_tin, 7.3F),
    BRONZE (R.string.bronze, R.mipmap.mat_bronze, 19.32F),
    CAST_IRON (R.string.cast_iron, R.mipmap.mat_cast_iron, 19.32F),
    PLATINUM (R.string.platinum, R.mipmap.mat_platinum, 19.32F),
    CHROME (R.string.chrome, R.mipmap.mat_chrome, 19.32F),
    GOLD (R.string.gold, R.mipmap.mat_gold, 19.32F);

    val unit = "kg/cm3"
}