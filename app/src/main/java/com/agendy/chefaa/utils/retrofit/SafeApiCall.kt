package com.agendy.chefaa.utils.retrofit


import com.agendy.chefaa.utils.exceptions.NoInternetConnectionException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ViewState<T> {
        return withContext(Dispatchers.IO) {
            try {
                ViewState.Success(apiCall.invoke())
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {

                        ViewState.Error(
                            code = e.code(),
                            message = e.message(),
                            exception = e,
                        )

                    }

                    is NoInternetConnectionException -> {
                        ViewState.NoInternetConnection
                    }

                    else -> {
                        ViewState.Error(exception = e)
                    }
                }
            }
        }
    }
}