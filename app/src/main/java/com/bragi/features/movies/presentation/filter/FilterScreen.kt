package com.bragi.features.movies.presentation.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bragi.R
import com.bragi.features.movies.presentation.model.GenreUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun FilterScreen(
    viewModel: FiltersViewModel = koinViewModel(),
    onGenreClicked: (GenreUi) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Content(uiState = uiState, onGenreClicked = onGenreClicked)
}

@Composable
fun Content(uiState: FiltersUiState, onGenreClicked: (GenreUi) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        if (uiState.isLoading) {
            Loader()
        } else if (uiState.error != null) {
            Error()
        } else {
            Genres(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                genres = uiState.genres,
                onGenreClicked = onGenreClicked
            )
        }
    }
}

@Composable
private fun Loader() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun Error() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.error), contentDescription = null
            )
            Text(
                text = "Error loading movies",
            )
        }
    }
}

@Composable
private fun Genres(
    modifier: Modifier,
    genres: List<GenreUi>,
    onGenreClicked: (GenreUi) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(genres) { genre ->
            GenreItem(genre, onGenreClicked = { onGenreClicked(it) })
        }
    }
}

@Composable
private fun GenreItem(
    genre: GenreUi,
    onGenreClicked: (GenreUi) -> Unit = {}
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onGenreClicked(genre) }
    ) {
        Text(genre.name)
    }
}

@Preview
@Composable
fun FilterScreenPreview() {
    Content(
        uiState = FiltersUiState(
            genres = listOf(
                GenreUi.All,
                GenreUi.Individual(1, "Action")
            )
        ), onGenreClicked = {})
}