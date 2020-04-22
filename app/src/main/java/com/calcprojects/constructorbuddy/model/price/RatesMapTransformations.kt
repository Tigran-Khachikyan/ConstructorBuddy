package com.calcprojects.constructorbuddy.model.price

import com.calcprojects.constructorbuddy.data.api_currency.Rates

fun getMapFromRates(rates: Rates): HashMap<String, Double>? = hashMapOf(
    "AED" to rates.AED,
    "AMD" to rates.AMD,
    "ARS" to rates.ARS,
    "AUD" to rates.AUD,
    "BOB" to rates.BOB,
    "BRL" to rates.BRL,
    "BYN" to rates.BYN,
    "CAD" to rates.CAD,
    "CHF" to rates.CHF,
    "CNY" to rates.CNY,
    "CUC" to rates.CUC,
    "DKK" to rates.DKK,
    "EGP" to rates.EGP,
    "EUR" to rates.EUR,
    "GBP" to rates.GBP,
    "GEL" to rates.GEL,
    "HKD" to rates.HKD,
    "IDR" to rates.IDR,
    "ILS" to rates.ILS,
    "INR" to rates.INR,
    "IQD" to rates.IQD,
    "IRR" to rates.IRR,
    "JPY" to rates.JPY,
    "KPW" to rates.KPW,
    "KRW" to rates.KRW,
    "KWD" to rates.KWD,
    "KZT" to rates.KZT,
    "LBP" to rates.LBP,
    "MAD" to rates.MAD,
    "MXN" to rates.MXN,
    "MYR" to rates.MYR,
    "NGN" to rates.NGN,
    "NOK" to rates.NOK,
    "NZD" to rates.NZD,
    "OMR" to rates.OMR,
    "PEN" to rates.PEN,
    "PHP" to rates.PHP,
    "PYG" to rates.PYG,
    "QAR" to rates.QAR,
    "RSD" to rates.RSD,
    "RUB" to rates.RUB,
    "SAR" to rates.SAR,
    "SEK" to rates.SEK,
    "SGD" to rates.SGD,
    "SYP" to rates.SYP,
    "THB" to rates.THB,
    "TMT" to rates.TMT,
    "TND" to rates.TND,
    "TRY" to rates.TRY,
    "UAH" to rates.UAH,
    "USD" to rates.USD,
    "UYU" to rates.UYU,
    "UZS" to rates.UZS,
    "VES" to rates.VES,
    "VND" to rates.VND,
    "ZAR" to rates.ZAR,
    "XAG" to rates.XAG,
    "XAU" to rates.XAU,
    "XPD" to rates.XPD,
    "XPT" to rates.XPT
)

fun getRatesFromMap(map: HashMap<String, Double>): Rates {
    val rates = Rates()

    rates.AED = map["AED"] ?: 0.0
    rates.AMD = map["AMD"] ?: 0.0
    rates.ARS = map["ARS"] ?: 0.0
    rates.AUD = map["AUD"] ?: 0.0
    rates.BOB = map["BOB"] ?: 0.0
    rates.BRL = map["BRL"] ?: 0.0
    rates.BYN = map["BYN"] ?: 0.0
    rates.CAD = map["CAD"] ?: 0.0
    rates.CHF = map["CHF"] ?: 0.0
    rates.CNY = map["CNY"] ?: 0.0
    rates.CUC = map["CUC"] ?: 0.0
    rates.DKK = map["DKK"] ?: 0.0
    rates.EGP = map["EGP"] ?: 0.0
    rates.EUR = map["EUR"] ?: 0.0
    rates.GBP = map["GBP"] ?: 0.0
    rates.GEL = map["GEL"] ?: 0.0
    rates.HKD = map["HKD"] ?: 0.0
    rates.IDR = map["IDR"] ?: 0.0
    rates.ILS = map["ILS"] ?: 0.0
    rates.INR = map["INR"] ?: 0.0
    rates.IQD = map["IQD"] ?: 0.0
    rates.IRR = map["IRR"] ?: 0.0
    rates.JPY = map["JPY"] ?: 0.0
    rates.KPW = map["KPW"] ?: 0.0
    rates.KRW = map["KRW"] ?: 0.0
    rates.KWD = map["KWD"] ?: 0.0
    rates.KZT = map["KZT"] ?: 0.0
    rates.LBP = map["LBP"] ?: 0.0
    rates.MAD = map["MAD"] ?: 0.0
    rates.MXN = map["MXN"] ?: 0.0
    rates.MYR = map["MYR"] ?: 0.0
    rates.NGN = map["NGN"] ?: 0.0
    rates.NOK = map["NOK"] ?: 0.0
    rates.NZD = map["NZD"] ?: 0.0
    rates.OMR = map["OMR"] ?: 0.0
    rates.PEN = map["PEN"] ?: 0.0
    rates.PHP = map["PHP"] ?: 0.0
    rates.PYG = map["PYG"] ?: 0.0
    rates.QAR = map["QAR"] ?: 0.0
    rates.RSD = map["RSD"] ?: 0.0
    rates.RUB = map["RUB"] ?: 0.0
    rates.SAR = map["SAR"] ?: 0.0
    rates.SEK = map["SEK"] ?: 0.0
    rates.SGD = map["SGD"] ?: 0.0
    rates.SYP = map["SYP"] ?: 0.0
    rates.THB = map["THB"] ?: 0.0
    rates.TMT = map["TMT"] ?: 0.0
    rates.TND = map["TND"] ?: 0.0
    rates.TRY = map["TRY"] ?: 0.0
    rates.UAH = map["UAH"] ?: 0.0
    rates.USD = map["USD"] ?: 0.0
    rates.UYU = map["UYU"] ?: 0.0
    rates.UZS = map["UZS"] ?: 0.0
    rates.VES = map["VES"] ?: 0.0
    rates.VND = map["VND"] ?: 0.0
    rates.ZAR = map["ZAR"] ?: 0.0
    rates.XAG = map["XAG"] ?: 0.0
    rates.XAU = map["XAU"] ?: 0.0
    rates.XPD = map["XPD"] ?: 0.0
    rates.XPT = map["XPT"] ?: 0.0
    return rates
}






