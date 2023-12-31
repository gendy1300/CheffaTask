package com.agendy.chefaa.main

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.hasKeyWithValueOfType
import com.agendy.chefaa.utils.logDebug
import com.agendy.chefaa.utils.navigation.NavHost
import com.agendy.chefaa.utils.navigation.NavigationIntent
import com.agendy.chefaa.utils.navigation.graphs.homeGraph
import com.agendy.chefaa.utils.navigation.screens.Destination
import com.agendy.chefaa.utils.navigation.screens.HomeScreens
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking


@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val workerState by mainViewModel.getWorkInfo(context = context).collectAsState(initial = null)


    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                mainViewModel.saveImageToApp(context = context, imageUri = uri){
                    mainViewModel.appNavigator.tryNavigateTo(HomeScreens.ResizeScreen(it))
                }
            }
        }

    NavigationEffects(
        navigationChannel = mainViewModel.navigationChannel, navHostController = navController
    )


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                content = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_upload),
                        contentDescription = null
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(it),
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

            logDebug(workerState?.firstOrNull()?.progress.toString())
            AnimatedVisibility(
                visible = workerState?.firstOrNull()?.progress?.hasKeyWithValueOfType<Int>("progress") == true,
                modifier = Modifier.fillMaxWidth()
            ) {
                workerState?.firstOrNull()?.progress?.let {
                    LinearProgressIndicator(
                        progress = it.getInt("progress", 0).toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    logDebug(it.getInt("progress", 0).toString())
                }

            }


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









