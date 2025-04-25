package com.bragi.movies.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bragi.R
import com.bragi.movies.domain.model.Movie
import com.bragi.movies.presentation.model.GenreStateUiModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MoviesScreenRoot(
    selectedGenre: GenreStateUiModel,
    viewModel: MoviesViewModel = koinViewModel(parameters = { parametersOf(selectedGenre) }),
    onFilterClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    MoviesScreen(uiState = uiState, onFilterClick)
}

@Composable
private fun MoviesScreen(uiState: MoviesUiState, onFilterClick: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { onFilterClick() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            LoadingView()
        } else if (uiState.error != null) {
            ErrorView()
        } else {
            ContentView(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                movies = uiState.movies
            )
        }
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ErrorView() {
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
private fun ContentView(modifier: Modifier, movies: List<Movie>) {
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
    MoviesScreen(
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
    MoviesScreen(
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
    MoviesScreen(
        uiState = MoviesUiState(
            isLoading = false,
            error = "Something went wrong",
            movies = emptyList()
        )
    )
}