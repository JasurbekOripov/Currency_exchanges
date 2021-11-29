package uz.juo.currencyexchanges.models

import java.io.Serializable

data class DataX(
    val buyCourse: Int,
    val cbCourse: Int,
    val currency: String,
    val currencyCode: String,
    val scale: Int,
    val sellCourse: Int
):Serializable