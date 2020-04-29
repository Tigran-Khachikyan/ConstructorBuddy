package com.calcprojects.constructorbuddy.model.figures

import androidx.room.*
import com.calcprojects.constructorbuddy.model.figures.Form.*

@Entity(tableName = "SHAPE")
@TypeConverters(FormTypeConverter::class)
data class Shape(
    val form: Form,
    var length: Double? = null,
    var par2: Double? = null,
    var par3: Double? = null,
    var par4: Double? = null,
    var par5: Double? = null

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shape_id")
    var id: Int = 0

    val area: Double?
        get() = when (form) {
            ANGLE -> par2?.let { w -> par3?.let { h -> par4?.let { t -> (w * h) - (w - t) * (h - t) } } } //ok
            BEAM -> par2?.let { w -> par3?.let { h -> par4?.let { t1 -> par5?.let { t2 -> 2 * w * t2 + (h - 2 * t2) * t1 } } } } //ok
            CHANNEL -> par2?.let { w -> par3?.let { h -> par4?.let { t -> (w * h) - (w - 2 * t) * (h - t) } } } //ok
            FLAT_BAR -> par2?.let { w -> par3?.let { h -> w * h } } //ok
            HEXAGONAL_BAR -> par2?.let { h -> 6 * (h / 2) * (h / 2) / kotlin.math.sqrt(3.0) }  //ok
            HEXAGONAL_HEX -> par2?.let { w ->
                par3?.let { t ->
                    (w / 2) * (w / 2) / kotlin.math.sqrt(3.0)
                    -(((w - 2 * t) / 2) * ((w - 2 * t) / 2) / kotlin.math.sqrt(3.0))        //ok
                }
            }
            HEXAGONAL_TUBE -> par2?.let { d ->
                par3?.let { s ->
                    3 * kotlin.math.sqrt(3.0) / 2 * s * s - (kotlin.math.PI * (d / 2) * (d / 2))   //ok
                }
            }
            PIPE -> par2?.let { d ->
                par3?.let { t ->
                    (kotlin.math.PI * (d / 2) * (d / 2)) - (kotlin.math.PI * ((d - t) / 2) * ((d - t) / 2))  //ok
                }
            }
            ROUND_BAR -> par2?.let { (kotlin.math.PI * (it / 2) * (it / 2)) } //ok
            SQUARE_BAR -> par2?.let { s -> s * s }
            SQUARE_TUBE -> par2?.let { w -> par3?.let { h -> par4?.let { t -> h * w - (h - 2 * t) * (w - 2 * t) } } } //ok
            T_BAR -> par2?.let { w -> par3?.let { h -> par4?.let { t -> w * t + (h - t) * t } } } //ok
        }

    val volume: Double?
        get() = area?.let { a -> length?.let { it * a } }
}