package com.agendy.chefaa.utils.exceptions

import okio.IOException

class NoInternetConnectionException : IOException() {
    override val message: String
        get() = "You are offline"
}