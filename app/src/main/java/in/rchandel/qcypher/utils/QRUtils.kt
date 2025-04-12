package `in`.rchandel.qcypher.utils

import com.google.gson.Gson
import `in`.rchandel.qcypher.data.model.QRResult

fun QRResult.toJson(): String = Gson().toJson(this)

fun String.toQRResult(): QRResult = Gson().fromJson(this, QRResult::class.java)