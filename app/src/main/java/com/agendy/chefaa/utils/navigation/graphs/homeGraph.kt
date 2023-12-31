package com.agendy.chefaa.utils.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.agendy.chefaa.imageList.presentation.ImageListScreen
import com.agendy.chefaa.imagePreview.presentation.ImagePreviewScreen
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