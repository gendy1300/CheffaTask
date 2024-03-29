package com.agendy.chefaa.utils.navigation.screens

sealed class Destination(
    protected val route: String,
    vararg params: String,
) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) :
        Destination(route) {
        operator fun invoke(): String = route
    }




    data object HomeGraph : NoArgumentsDestination(HOME_ROUTE)




    companion object {
        private const val HOME_ROUTE = HomeScreens.ROOT_ROUTE

    }
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}
