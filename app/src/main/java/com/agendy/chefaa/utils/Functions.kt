package com.agendy.chefaa.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalTime


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun logDebug(message: String, tag: String = "Chefaa") {
    Log.d(tag, message)
}


 fun generateMarvelHash(): String {
    val privateKey = "4711bb7cc52825d3c4f31d4b8cf759898036d363"
    val publicKey = "b72936ddcd6fa136c819b8765bc86845"
    val input = getCurrentHourUsingLocalTime() + privateKey + publicKey
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(input.toByteArray())
    return BigInteger(1, digest).toString(16).padStart(32, '0')
}

fun getCurrentHourUsingLocalTime(): String {
    val currentTime = LocalTime.now()
    return currentTime.hour.toString()
}
