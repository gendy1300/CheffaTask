package com.agendy.chefaa.utils.navigation.graphs

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.agendy.chefaa.imageList.presentation.ImageListScreen
import com.agendy.chefaa.imagePreview.presentation.ImagePreviewScreen
import com.agendy.chefaa.utils.getActivity
import com.agendy.chefaa.utils.navigation.composable
import com.agendy.chefaa.utils.navigation.screens.HomeScreens

fun NavGraphBuilder.homeGraph(

) {
    navigation(
        startDestination = HomeScreens.ImagesScreen.fullRoute,
        route = HomeScreens.ROOT_ROUTE
    ) {
        composable(destination = HomeScreens.ImagesScreen) {
            ImageListScreen()
        }


        composable(
            destination = HomeScreens.ResizeScreen,
            deepLinks = listOf(navDeepLink {
                action = "android.intent.action.SEND"
            })
        ) {
            ImagePreviewScreen()
        }

    }


}