package com.agendy.chefaa.main

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.agendy.chefaa.utils.navigation.NavHost
import com.agendy.chefaa.utils.navigation.NavigationIntent
import com.agendy.chefaa.utils.navigation.graphs.homeGraph
import com.agendy.chefaa.utils.navigation.screens.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking


@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()




    NavigationEffects(
        navigationChannel = mainViewModel.navigationChannel, navHostController = navController
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {

        NavHost(
            navController = navController,
            startDestination = runBlocking {
                Destination.HomeGraph
            },
            Modifier,

            ) {

            homeGraph()


        }

    }

}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>, navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}







