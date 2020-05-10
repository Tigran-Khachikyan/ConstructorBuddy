package com.calcprojects.constructorbuddy.model.price

import androidx.room.*
import com.calcprojects.constructorbuddy.model.units.Unit


@Entity(tableName = "PRICE")
@TypeConverters(CurrencyTypeConverter::class)
data class Price(
    var base: Currency,
    var value: Double
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "price_id")
    var id: Int = 0
}
