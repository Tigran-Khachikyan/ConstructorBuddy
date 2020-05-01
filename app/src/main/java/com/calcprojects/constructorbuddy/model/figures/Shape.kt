package com.calcprojects.constructorbuddy.model.figures

import androidx.room.*
import com.calcprojects.constructorbuddy.model.figures.Form.*

@Entity(tableName = "SHAPE")
@TypeConverters(FormTypeConverter::class)
data class Shape(
    val form: Form
) {

    var length: Double? = null
    var width: Double? = null
    var height: Double? = null
    var thickness: Double? = null
    var thickness2: Double? = null
    var diameter: Double? = null
    var side: Double? = null

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shape_id")
    var id: Int = 0

    val area: Double?
        get() = when (form) {
            ANGLE -> width?.let { w -> height?.let { h -> thickness?.let { t -> (w * h) - (w - t) * (h - t) } } } //ok
            BEAM -> width?.let { w -> height?.let { h -> thickness?.let { t1 -> thickness2?.let { t2 -> 2 * w * t2 + (h - 2 * t2) * t1 } } } } //ok
            CHANNEL -> width?.let { w -> height?.let { h -> thickness?.let { t -> (w * h) - (w - 2 * t) * (h - t) } } } //ok
            FLAT_BAR -> width?.let { w -> height?.let { h -> w * h } } //ok
            HEXAGONAL_BAR -> height?.let { h -> 6 * (h / 2) * (h / 2) / kotlin.math.sqrt(3.0) }  //ok
            HEXAGONAL_HEX -> width?.let { w ->
                thickness?.let { t ->
                    (w / 2) * (w / 2) / kotlin.math.sqrt(3.0)
                    -(((w - 2 * t) / 2) * ((w - 2 * t) / 2) / kotlin.math.sqrt(3.0))        //ok
                }
            }
            HEXAGONAL_TUBE -> diameter?.let { d ->
                side?.let { s ->
                    3 * kotlin.math.sqrt(3.0) / 2 * s * s - (kotlin.math.PI * (d / 2) * (d / 2))   //ok
                }
            }
            PIPE -> diameter?.let { d ->
                thickness?.let { t ->
                    (kotlin.math.PI * (d / 2) * (d / 2)) - (kotlin.math.PI * ((d - t) / 2) * ((d - t) / 2))  //ok
                }
            }
            ROUND_BAR -> diameter?.let { (kotlin.math.PI * (it / 2) * (it / 2)) } //ok
            SQUARE_BAR -> side?.let { s -> s * s }
            SQUARE_TUBE -> width?.let { w -> height?.let { h -> thickness?.let { t -> h * w - (h - 2 * t) * (w - 2 * t) } } } //ok
            T_BAR -> width?.let { w -> height?.let { h -> thickness?.let { t -> w * t + (h - t) * t } } } //ok
        }

    val volume: Double?
        get() = area?.let { a -> length?.let { it * a } }
}