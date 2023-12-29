package com.agendy.chefaa.imagePreview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewIntents
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewModel
import com.agendy.chefaa.utils.BackIcon
import com.agendy.chefaa.utils.CustomTextField
import com.agendy.chefaa.utils.getActivity

@Composable
fun ImagePreviewScreen(viewModel: ImagePreviewViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val currentActivity = context.getActivity()

    val lifecycleOwner = LocalLifecycleOwner.current


    val imageModel by viewModel.imageModel
    val dominantColor by viewModel.dominantColor


    LaunchedEffect(Unit) {
        currentActivity?.let { activity ->
            viewModel.processIntent(
                ImagePreviewViewIntents.GetImage(
                    lifecycleOwner = lifecycleOwner,
                    currentActivity = activity,
                    context = context
                )
            )
        }
    }

    ImagePreviewComponent(imageModel, dominantColor) {
        viewModel.processIntent(ImagePreviewViewIntents.NavigateBack)
    }

}


@Composable
fun ImagePreviewComponent(
    imageModel: ImageModel,
    dominantColor: Color,
    onBackPresses: () -> Unit
) {

    var caption by remember {
        mutableStateOf(imageModel.imageCaption)
    }

    Column(
        modifier = Modifier
            .background(color = dominantColor)
            .fillMaxSize()
            .padding(top = 57.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        BackIcon(modifier = Modifier.align(Alignment.Start)) {
            onBackPresses()
        }

        AsyncImage(model = imageModel.imagePath, contentDescription = null)


        CustomTextField(
            value = caption, onValueChange = {
                caption = it

            }, modifier = Modifier.fillMaxWidth(),
            isError = caption.contentEquals("No Caption")
        )


    }
}