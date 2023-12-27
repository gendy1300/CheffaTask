package com.agendy.chefaa.imageList.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agendy.chefaa.R
import com.agendy.chefaa.imageList.data.model.ImageModel
import com.agendy.chefaa.imageList.viewmodel.ImagesViewIntent
import com.agendy.chefaa.imageList.viewmodel.ImagesViewModel
import com.agendy.chefaa.utils.TextWithFont
import com.agendy.chefaa.utils.retrofit.HandleState

@Composable
fun ImageListScreen(viewModel: ImagesViewModel = hiltViewModel()) {

    val imagesState by viewModel.imagesResponse
    val context = LocalContext.current

    val offlineImages by viewModel.images.collectAsState(initial = listOf())

    LaunchedEffect(Unit) {
        viewModel.processIntent(ImagesViewIntent.CallCharacters)

    }

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

    ImagesListComponent(images = offlineImages)

}


@Composable
fun ImagesListComponent(images: List<ImageModel>?) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), verticalItemSpacing = 10.dp) {
        images?.let {
            items(it, key = { it.id }) { item ->
                ImageItem(item)
            }
        }

    }
}


@Composable
fun ImageItem(item: ImageModel) {
    ElevatedCard(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column {
            AsyncImage(
                model = item.imagePath,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),

                contentScale = ContentScale.FillBounds
            )

            TextWithFont(
                text = if (item.imageCaption.isNotEmpty()) item.imageCaption else stringResource(
                    R.string.no_caption
                )
            )
        }

    }

}