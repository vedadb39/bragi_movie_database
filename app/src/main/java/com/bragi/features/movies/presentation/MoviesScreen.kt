package com.bragi.features.movies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import coil3.compose.AsyncImage
import com.bragi.R
import com.bragi.features.movies.domain.model.Movie
import com.bragi.features.movies.presentation.model.GenreUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MoviesScreen(
    genreUi: GenreUi,
    viewModel: MoviesViewModel = koinViewModel(parameters = { parametersOf(genreUi) }),
    onFilterClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Content(uiState = uiState, onFilterClick)
}

@Composable
private fun Content(uiState: MoviesUiState, onFilterClick: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { onFilterClick() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Loader()
        } else if (uiState.error != null) {
            Error()
        } else {
            Movies(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                movies = uiState.movies
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
private fun Movies(modifier: Modifier, movies: List<Movie>) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3)
    ) {
        // Pagination????
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
private fun MovieItem(movie: Movie) {
    Column {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.movie_placeholder)
        )
        Text(text = movie.title)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Rating: ${movie.rating}")
        Text(text = "Revenue: ${movie.revenue}")
        Text(text = "Budget: ${movie.budget}")

    }
}

@Preview
@Composable
fun MoviesScreenLoadingPreview() {
    Content(
        uiState = MoviesUiState(
            isLoading = true,
            error = null,
            movies = emptyList()
        )
    )
}

@Preview
@Composable
fun MoviesScreenPreview() {
    Content(
        uiState = MoviesUiState(
            isLoading = false,
            error = null,
            movies = List(5) {
                Movie(
                    id = 1,
                    title = "Movie 1",
                    posterUrl = "",
                    rating = 5.0F,
                    revenue = 1000000,
                    budget = 500000
                )
            }
        )
    )
}

@Preview
@Composable
fun MoviesScreenErrorPreview() {
    Content(
        uiState = MoviesUiState(
            isLoading = false,
            error = "Something went wrong",
            movies = emptyList()
        )
    )
}