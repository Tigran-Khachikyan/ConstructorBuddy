package com.calcprojects.constructorbuddy.model

import android.util.Log
import androidx.room.*
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Shape
import com.calcprojects.constructorbuddy.model.units.Unit
import com.calcprojects.constructorbuddy.model.units.UnitTypeConverter
import com.calcprojects.constructorbuddy.model.units.fromGCm3ToLbIn3

@Entity(tableName = "MODELS")
@TypeConverters(UnitTypeConverter::class)
class Model constructor(
    @Embedded
    val shape: Shape,
    @Embedded
    val material: Material,
    val units: Unit,
    var weight: Double

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0

    companion object {
        fun createByLength(shape: Shape, material: Material, units: Unit): Model? {

            val weight = if (units == Unit.METRIC)
                shape.run { volume?.let { material.substance.density * it / 1000 } }
            else
                shape.run { volume?.let { fromGCm3ToLbIn3(material.substance.density) * it } }

            Log.d("ashjhs","weight: $weight")
            Log.d("ashjhs","volume: ${shape.volume}")


            return weight?.let { Model(shape, material, units, it) }
        }

        fun createByWeight(shape: Shape, material: Material, units: Unit, weight: Double): Model? {

            shape.length = if (units == Unit.METRIC)
                shape.area?.let { 1000 * weight / (it * material.substance.density) }
            else
                shape.area?.let { weight / (it * fromGCm3ToLbIn3(material.substance.density)) }


            Log.d("ashjhs","shape.area: ${shape.area}")

            return shape.length?.let { Model(shape, material, units, weight) }
        }
    }
}


/*sealed class Builder(
    private var shape: Shape? = null,
    protected var material: Material? = null,
    protected var area: Double? = null,
    protected var length: Double? = null,
    protected var weight: Double? = null,
    private var par2: Double? = null,
    private var par3: Double? = null,
    private var par4: Double? = null,
    private var par5: Double? = null
) {

    fun shape(shape: Shape) = apply { this.shape = shape }
    fun material(material: Material) = apply { this.material = material }
    fun param2(par2: Double) = apply { this.par2 = par2 }
    fun param3(par3: Double?) = apply { this.par3 = par3 }
    fun param4(par4: Double?) = apply { this.par4 = par4 }
    fun param5(par5: Double?) = apply { this.par5 = par5 }

    private fun area() {
        area = shape?.let {
            when (it) {
                ANGLE -> par2?.run { this * this }
                BEAM -> par2?.run { this * this }
                CHANNEL -> par2?.run { this * this }
                FLAT_BAR -> par2?.run { this * this }
                HEXAGONAL_BAR -> par2?.run { this * this }
                HEXAGONAL_HEX -> par2?.run { this * this }
                HEXAGONAL_TUBE -> par2?.run { this * this }
                PIPE -> par2?.run { this * this }
                ROUND_BAR -> par2?.run { this * this }
                SQUARE_BAR -> par2?.run { this * this }
                SQUARE_TUBE -> par2?.run { this * this }
                T_BAR -> par2?.run { this * this }
            }
        }
    }

    protected abstract fun clarify()

    fun build(): Model? {
        area()
        clarify()
        return if (shape != null && material != null && area != null && length != null && weight != null)
            Model(shape!!, material!!, area!!, length!!, weight!!)
        else null
    }

    class ByLength(length: Double) : Builder(length = length) {
        override fun clarify() {
            area?.let { ar ->
                material?.let { m ->
                    length?.let { weight = ar * m.density * it }
                }
            }
        }
    }

    class ByWeight(weight: Double) : Builder(weight = weight) {
        override fun clarify() {
            area?.let { ar ->
                material?.let { m ->
                    weight?.let { w -> length = w / (ar * m.density) }
                }
            }
        }
    }

}*/

