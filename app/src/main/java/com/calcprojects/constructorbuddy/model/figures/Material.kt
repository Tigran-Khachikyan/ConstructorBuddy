package com.calcprojects.constructorbuddy.model.figures

import androidx.room.*
import com.calcprojects.constructorbuddy.model.price.Price

@Entity(tableName = "MATERIAL")
@TypeConverters(SubstanceTypeConverter::class)
data class Material(
    val substance: Substance,
    @Embedded
    val price: Price? = null
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "material_id")
    var id: Int = 0
}