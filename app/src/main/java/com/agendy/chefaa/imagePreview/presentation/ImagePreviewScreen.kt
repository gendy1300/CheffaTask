package com.agendy.chefaa.imagePreview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agendy.chefaa.R
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewIntents
import com.agendy.chefaa.imagePreview.viewmodel.ImagePreviewViewModel
import com.agendy.chefaa.utils.AppButton
import com.agendy.chefaa.utils.BackIcon
import com.agendy.chefaa.utils.CustomTextField
import com.agendy.chefaa.utils.LabelText
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

    ImagePreviewComponent(
        imageModel,
        dominantColor,
        onSubmitClicked = { width: Int, height: Int, caption ->


            viewModel.processIntent(
                ImagePreviewViewIntents.StartWorker(
                    context = context,
                    imageId = imageModel.id,
                    width = width,
                    height = height,
                    caption = caption,
                    isCaptionOnly = width == imageModel.width && height == imageModel.height
                )
            )
        },
        onBackPresses = {
            viewModel.processIntent(ImagePreviewViewIntents.NavigateBack)
        })
}


@Composable
fun ImagePreviewComponent(
    imageModel: ImageModel,
    dominantColor: Color,
    onSubmitClicked: (width: Int, height: Int, caption: String) -> Unit,
    onBackPresses: () -> Unit,
) {

    var caption by remember {
        mutableStateOf(imageModel.imageCaption)
    }

    var height by remember {
        mutableStateOf(imageModel.height.toString())
    }

    var width by remember {
        mutableStateOf(imageModel.width.toString())
    }

    LaunchedEffect(imageModel) {
        caption = imageModel.imageCaption
        height = imageModel.height.toString()
        width = imageModel.width.toString()
    }
    Column(
        modifier = Modifier
            .background(color = dominantColor)
            .fillMaxSize()
            .padding(top = 57.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
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
            isError = caption.contentEquals(stringResource(id = R.string.no_caption)),
            label = {
                LabelText(stringResource(id = R.string.caption))
            }
        )



        CustomTextField(
            value = height, onValueChange = {
                try {
                    height = if (it.isBlank())
                        "0"
                    else {
                        if (height == "0")
                            it.dropLast(1).toInt().toString()
                        else
                            it.toInt().toString()
                    }
                } catch (_: NumberFormatException) {

                }


            }, modifier = Modifier.fillMaxWidth(),
            label = {
                LabelText(stringResource(id = R.string.height))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )


        CustomTextField(
            value = width, onValueChange = {

                try {
                    width = if (it.isBlank())
                        "0"
                    else {
                        if (width == "0")
                            it.dropLast(1).toInt().toString()
                        else
                            it.toInt().toString()
                    }
                } catch (_: NumberFormatException) {

                }
            }, modifier = Modifier.fillMaxWidth(),
            label = {
                LabelText(stringResource(id = R.string.width))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )


        AppButton(text = stringResource(R.string.submit)) {
            onSubmitClicked(width.toInt(), height.toInt(), caption)
        }

    }
}