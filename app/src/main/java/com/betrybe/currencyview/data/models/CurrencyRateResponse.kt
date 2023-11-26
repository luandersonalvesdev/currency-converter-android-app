package com.betrybe.currencyview.data.models

data class CurrencyRateResponse(
    val success: Boolean,
    val base: String,
    val data: String,
    val rates: Map<String, Double>
)
