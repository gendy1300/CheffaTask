package com.agendy.chefaa.utils.retrofit

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.agendy.chefaa.utils.ErrorComponent
import com.agendy.chefaa.utils.LoadingComponent
import com.agendy.chefaa.utils.logDebug

sealed class ViewState<out T> {

    data object NotCalled : ViewState<Nothing>()
    data object Loading : ViewState<Nothing>()

    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val exception: Exception? = null
    ) :
        ViewState<Nothing>()

    data object NoInternetConnection :
        ViewState<Nothing>()
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun <T> ViewState<T>.HandleState(
    showError: Boolean = true,
    logoutUnauthorized: Boolean = true,
    onSuccess: @Composable (data: T) -> Unit,
    onError: ((exception: Exception) -> Unit)? = null
) {


    when (this) {
        is ViewState.Error -> {

            if (showError)
                exception?.let { ErrorComponent(it, mutableStateOf(true)) }
            if (onError != null) {
                exception?.let { onError(it) }
            }

            logDebug(exception?.stackTraceToString().toString())
        }

        ViewState.Loading -> {
            LoadingComponent()
        }

        ViewState.NoInternetConnection -> {
            logDebug("Error ... ")
        }

        is ViewState.Success -> {
            onSuccess(this.data)
        }

        ViewState.NotCalled -> {}

    }
}


fun <T> ViewState<T>.shouldCallApi(): Boolean {
    return when (this) {
        is ViewState.Error -> {
            if (this.code == 401)
                false
            else
                false
        }

        ViewState.Loading -> false
        ViewState.NoInternetConnection -> true
        ViewState.NotCalled -> true
        is ViewState.Success -> false
    }

}

