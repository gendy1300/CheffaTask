package com.agendy.chefaa.utils.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.agendy.chefaa.imageList.presentation.ImageListScreen
import com.agendy.chefaa.utils.navigation.composable
import com.agendy.chefaa.utils.navigation.screens.HomeScreens

fun NavGraphBuilder.homeGraph(

) {
    navigation(
        startDestination = HomeScreens.ImagesScreen.fullRoute,
        route = HomeScreens.ROOT_ROUTE
    ) {
        composable(HomeScreens.ImagesScreen) {
            ImageListScreen()
        }


//        composable(HomeScreens.StationDetailsScreen) {
//            isBottomSheetVisibleState.value = false
//            StationDetailsScreen()
//        }

    }


}