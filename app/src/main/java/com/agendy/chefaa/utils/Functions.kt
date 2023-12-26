package com.agendy.chefaa.utils

import android.content.Context
import android.util.Log
import android.widget.Toast


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun logDebug(message: String, tag: String = "Chefaa") {
    Log.d(tag, message)
}



