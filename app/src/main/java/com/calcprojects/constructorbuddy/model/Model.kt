package com.calcprojects.constructorbuddy.model

import android.content.Context
import android.util.Log
import androidx.room.*
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Shape
import com.calcprojects.constructorbuddy.model.units.Unit
import com.calcprojects.constructorbuddy.model.units.UnitTypeConverter
import com.calcprojects.constructorbuddy.model.units.fromGCm3ToLbIn3
import com.calcprojects.constructorbuddy.ui.name

@Entity(tableName = "MODELS")
@TypeConverters(UnitTypeConverter::class)
class Model constructor(
    @Embedded
    val shape: Shape,
    @Embedded
    val material: Material,
    val units: Unit,
    var weight: Double,
    val createdByLength: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0

    var name: String? = null

    companion object {
        fun createByLength(shape: Shape, material: Material, units: Unit): Model? {

            val weight = if (units == Unit.METRIC)
                shape.run { volume?.let { material.substance.density * it / 1000 } }
            else
                shape.run { volume?.let { fromGCm3ToLbIn3(material.substance.density) * it } }

            return weight?.let { Model(shape, material, units, it, true) }
        }

        fun createByWeight(shape: Shape, material: Material, units: Unit, weight: Double): Model? {

            shape.length = if (units == Unit.METRIC)
                shape.area?.let { 1000 * weight / (it * material.substance.density) }
            else
                shape.area?.let { weight / (it * fromGCm3ToLbIn3(material.substance.density)) }

            return shape.length?.let { Model(shape, material, units, weight, false) }
        }
    }

    fun getResultToSend(context: Context): String {
        return this.run {
            val nameText = name?.let { "Model name: $it" } ?: ""
            val shapeText = shape.let {
                "${(R.string.shape).name(context)}: ${it.form.nameRes.name(context)}"
            }
            val matText = material.let {
                "${(R.string.material).name(context)}: ${it.substance.nameRes.name(context)}"
            }
            nameText + "\n" + shapeText + "\n" + matText
        }
    }
}

