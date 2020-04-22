package com.calcprojects.constructorbuddy.model.figures

import androidx.room.TypeConverter
import com.calcprojects.constructorbuddy.R

enum class Form(val nameRes: Int, val imageRes: Int, val markedImageRes: Int) {
    ROUND_BAR(R.string.roundBar, R.mipmap.roundbar, R.mipmap.roundbar_prof),
    SQUARE_BAR(R.string.square_bar, R.mipmap.square_bar, R.mipmap.square_bar_prof),
    SQUARE_TUBE(R.string.square_tubing, R.mipmap.square_tubing, R.mipmap.square_tubing_prof),
    ANGLE(R.string.angle, R.mipmap.angle, R.mipmap.angle_prof),
    BEAM(R.string.beam, R.mipmap.beam, R.mipmap.beam_prof),
    CHANNEL(R.string.channel, R.mipmap.channel, R.mipmap.channel_prof),
    FLAT_BAR(R.string.flat_bar, R.mipmap.flat_bar, R.mipmap.flat_bar_prof),
    HEXAGONAL_BAR(R.string.hexagonal_bar, R.mipmap.hexagonal_bar, R.mipmap.hexagonal_bar_prof),
    HEXAGONAL_TUBE(R.string.hexagonal_tube, R.mipmap.hexagonal_tube, R.mipmap.hexagonal_tube_prof),
    HEXAGONAL_HEX(R.string.hexagonal_hex, R.mipmap.hexagonal_hex, R.mipmap.hexagonal_hex_prof),
    PIPE(R.string.pipe, R.mipmap.pipe, R.mipmap.pipe_prof),
    T_BAR(R.string.t_bar, R.mipmap.t_bar, R.mipmap.t_bar_prof)
}

class FormTypeConverter {

    @TypeConverter
    fun getFormByNameRes(res: Int): Form? = Form.values().find { f -> f.nameRes == res }

    @TypeConverter
    fun getCurrencyName(form: Form): Int = form.nameRes

   /* @TypeConverter
    fun getFormByImageRes(res: Int): Form? = Form.values().find { f -> f.imageRes == res }

    @TypeConverter
    fun getCurrencyImage(form: Form): Int = form.imageRes

    @TypeConverter
    fun getFormByMarkedImage(res: Int): Form? = Form.values().find { f -> f.markedImageRes == res }

    @TypeConverter
    fun getCurrencyMarkedImage(form: Form): Int = form.markedImageRes*/

}