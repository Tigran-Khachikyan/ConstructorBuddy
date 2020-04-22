package com.calcprojects.constructorbuddy.model.price

import androidx.room.TypeConverter
import com.calcprojects.constructorbuddy.R

enum class Currency(val nameRes: Int) {

    AED(R.string.AED),
    AMD(R.string.AMD),
    ARS(R.string.ARS),
    AUD(R.string.AUD),
    BOB(R.string.BOB),
    BRL(R.string.BRL),
    BYN(R.string.BYN),
    CAD(R.string.CAD),
    CHF(R.string.CHF),
    CNY(R.string.CNY),
    CUC(R.string.CUC),
    DKK(R.string.DKK),
    EGP(R.string.EGP),
    EUR(R.string.EUR),
    GBP(R.string.GBP),
    GEL(R.string.GEL),
    HKD(R.string.HKD),
    IDR(R.string.IDR),
    ILS(R.string.ILS),
    INR(R.string.INR),
    IQD(R.string.IQD),
    IRR(R.string.IRR),
    JPY(R.string.JPY),
    KPW(R.string.KPW),
    KRW(R.string.KRW),
    KWD(R.string.KWD),
    KZT(R.string.KZT),
    LBP(R.string.LBP),
    MAD(R.string.MAD),
    MXN(R.string.MXN),
    MYR(R.string.MYR),
    NGN(R.string.NGN),
    NOK(R.string.NOK),
    NZD(R.string.NZD),
    OMR(R.string.OMR),
    PEN(R.string.PEN),
    PHP(R.string.PHP),
    PYG(R.string.PYG),
    QAR(R.string.QAR),
    RSD(R.string.RSD),
    RUB(R.string.RUB),
    SAR(R.string.SAR),
    SEK(R.string.SEK),
    SGD(R.string.SGD),
    SYP(R.string.SYP),
    THB(R.string.THB),
    TMT(R.string.TMT),
    TND(R.string.TND),
    TRY(R.string.TRY),
    UAH(R.string.UAH),
    USD(R.string.USD),
    UYU(R.string.UYU),
    UZS(R.string.UZS),
    VES(R.string.VES),
    VND(R.string.VND),
    ZAR(R.string.ZAR)
}

class CurrencyTypeConverter {

    @TypeConverter
    fun getCurrency(res: Int): Currency? = Currency.values().find { c -> c.nameRes == res }

    @TypeConverter
    fun getCurrencyName(currency: Currency): Int = currency.nameRes

}