package com.agendy.chefaa.imageList.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agendy.chefaa.imageList.data.model.Result
import com.agendy.chefaa.imageList.viewmodel.ImagesViewIntent
import com.agendy.chefaa.imageList.viewmodel.ImagesViewModel
import com.agendy.chefaa.utils.retrofit.HandleState

@Composable
fun ImageListScreen(viewModel: ImagesViewModel = hiltViewModel()) {

    val imagesState by viewModel.imagesResponse
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(ImagesViewIntent.CallCharacters)
    }

    imagesState.HandleState(onSuccess = {
        it.data?.results?.get(0)?.thumbnail?.apply {
            viewModel.processIntent(
                ImagesViewIntent.DownloadImage(
                    path +"." +extension,
                    it.data.results.get(0).id.toString(),
                    context
                )
            )
        }

        ImagesListComponent(it.data?.results)
    })

}


@Composable
fun ImagesListComponent(results: List<Result>?) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        results?.let {
            items(it) { item ->
                AsyncImage(
                    model = "${item.thumbnail?.path}.${item.thumbnail?.extension}",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),

                    contentScale = ContentScale.FillBounds
                )
            }
        }

    }
}