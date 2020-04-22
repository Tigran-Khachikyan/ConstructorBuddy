package com.calcprojects.constructorbuddy.model.figures

import androidx.room.*
import com.calcprojects.constructorbuddy.model.figures.Form.*

@Entity(tableName = "SHAPE")
@TypeConverters(FormTypeConverter::class)
data class Shape(
    val form: Form,
    var length: Double? = null,
    var width: Double? = null,
    var height: Double? = null,
    var thickness: Double? = null,
    var thickness2: Double? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shape_id")
    var id: Int = 0
) {

    val area: Double?
        get() = when (form) {
            ANGLE -> width?.let { w -> height?.let { h -> w * h } }
            BEAM -> width?.let { w -> height?.let { h -> w * h } }
            CHANNEL -> width?.let { w -> height?.let { h -> w * h } }
            FLAT_BAR -> width?.let { w -> height?.let { h -> w * h } }
            HEXAGONAL_BAR -> width?.let { w -> height?.let { h -> w * h } }
            HEXAGONAL_HEX -> width?.let { w -> height?.let { h -> w * h } }
            HEXAGONAL_TUBE -> width?.let { w -> height?.let { h -> w * h } }
            PIPE -> width?.let { w -> height?.let { h -> w * h } }
            ROUND_BAR -> width?.let { w -> height?.let { h -> w * h } }
            SQUARE_BAR -> width?.let { w -> height?.let { h -> w * h } }
            SQUARE_TUBE -> width?.let { w -> height?.let { h -> w * h } }
            T_BAR -> width?.let { w -> height?.let { h -> w * h } }
        }

    val volume: Double?
        get() = area?.let { a -> length?.let { it * a } }
}