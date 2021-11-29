package uz.juo.currencyexchanges.models

import uz.juo.currencyexchanges.models.DataX

data class Data(
    val `data`: List<DataX>,
    val error_message: String,
    val status: String,
    val timestamp: Long
)