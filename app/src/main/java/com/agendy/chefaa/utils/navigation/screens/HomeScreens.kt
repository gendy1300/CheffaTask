package com.agendy.chefaa.utils.navigation.screens


sealed class HomeScreens {

    data object ImagesScreen : Destination.NoArgumentsDestination(IMAGES_SCREEN_ROUTE)


    data object ResizeScreen : Destination(RESIZE_SCREEN_ROUTE, IMAGE_ID_KEY) {
        operator fun invoke(imageId: Int): String = route.appendParams(
            IMAGE_ID_KEY to imageId,
        )
    }

    companion object {
        const val ROOT_ROUTE = "home"
        private const val IMAGES_SCREEN_ROUTE = "imagesScreen"
        private const val RESIZE_SCREEN_ROUTE = "resize"

        const val IMAGE_ID_KEY = "imageKey"
    }
}

