package com.calcprojects.constructorbuddy.model.price

import androidx.room.*


@Entity(tableName = "PRICE")
@TypeConverters(CurrencyTypeConverter::class)
data class Price(
    var base: Currency,
    var value: Double,
    var unit: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "price_id")
    var id: Int = 0
}
