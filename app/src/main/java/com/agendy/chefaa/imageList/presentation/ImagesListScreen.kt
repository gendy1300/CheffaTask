package com.agendy.chefaa.imageList.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agendy.chefaa.R
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.viewmodel.ImagesViewIntent
import com.agendy.chefaa.imageList.viewmodel.ImagesViewModel
import com.agendy.chefaa.utils.CustomTextField
import com.agendy.chefaa.utils.TextWithFont
import com.agendy.chefaa.utils.retrofit.HandleState
import com.agendy.chefaa.utils.retrofit.shouldCallApi
import com.agendy.chefaa.utils.theme.LightBlack


@Composable
fun ImageListScreen(viewModel: ImagesViewModel = hiltViewModel()) {

    val imagesState by viewModel.imagesResponse
    val context = LocalContext.current

    val offlineImages by viewModel.images.collectAsState(initial = listOf())

    LaunchedEffect(Unit) {
        if (imagesState.shouldCallApi())
            viewModel.processIntent(ImagesViewIntent.CallCharacters)

    }

    /**
     * This was implemented to load the images every time because
     * if a new image is uploaded it will be added to the database
     */
    imagesState.HandleState(onSuccess = {
        it.data?.results?.let { results ->
            viewModel.processIntent(
                ImagesViewIntent.DownloadImage(
                    results,
                    context
                )
            )
        }
    })


    ImagesListComponent(images = offlineImages, onImageClicked = { id ->
        viewModel.processIntent(ImagesViewIntent.NavigateToImagePreview(id))
    }, onSave = { id ->
        viewModel.processIntent(ImagesViewIntent.SaveImage(id, context))
    })

}


/**
 * A Staggered Grid was used to make the items have different heights to get
 * a feeling of the aspect ratio of the image
 */
@Composable
fun ImagesListComponent(
    images: List<ImageModel>?,
    onImageClicked: (id: Int) -> Unit,
    onSave: (imageId: Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),

        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var searchQuery by remember {
            mutableStateOf("")
        }

        Header(searchQuery) {
            searchQuery = it
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val previewList = if (searchQuery.isNotBlank()) {
                images?.filter {
                    it.imageCaption.contains(searchQuery, true)
                }
            } else
                images

            previewList?.let {
                items(it, key = { item ->
                    item.id
                }) { item ->
                    ImageItem(item, onImageClicked, onSave = onSave)
                }
            }

        }

    }


}


/**
 * It can make the image fill the bounds of the photo but to see the image aspect ratio
 * the content scale wasn't set
 */
@Composable
fun ImageItem(item: ImageModel, onImageClicked: (id: Int) -> Unit, onSave: (imageId: Int) -> Unit) {

    var isMenuVisible by remember { mutableStateOf(false) }
    Column(
        Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isMenuVisible = true
                    },
                    onTap = {
                        onImageClicked(item.id)
                    },
                )
            }) {
        AsyncImage(
            model = item.imagePath,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),

            /*contentScale = ContentScale.FillBounds*/
        )

        TextWithFont(
            text = if (item.imageCaption.length > 80) {
                "${item.imageCaption.take(80)}..."
            } else {
                item.imageCaption
            },
            color = Color.White
        )


        DropdownMenu(
            expanded = isMenuVisible,
            onDismissRequest = {
                isMenuVisible = false
            },
            modifier = Modifier
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        isMenuVisible = false
                        onSave(item.id)
                    }
                    .padding(8.dp),
                text = stringResource(R.string.save_image)
            )
        }
    }

}


@Composable
fun Header(searchValue: String, onValueChange: (String) -> Unit) {
    var isSearchVisible by remember {
        mutableStateOf(false)
    }

    val searchFieldFocus = remember { FocusRequester() }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LightBlack),
        shape = RoundedCornerShape(0.dp)

    ) {

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(15.dp), verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .clickable {
                        isSearchVisible = !isSearchVisible
                    }
            )
            AnimatedVisibility(visible = isSearchVisible) {

                SideEffect {
                    searchFieldFocus.requestFocus()
                }

                CustomTextField(
                    value = searchValue, onValueChange = onValueChange,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_notification_clear_all),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onValueChange("")
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(searchFieldFocus)
                        .height(50.dp),
                    textStyle = TextStyle(
                        fontSize = 9.sp,

                        ),

                    )
            }

        }
    }
}