package com.bragi.movies.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bragi.movies.domain.model.Movie
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreenRoot(
    viewModel: MoviesViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    MoviesScreen(movies = uiState.movies)
}

@Composable
fun MoviesScreen(movies: List<Movie>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->

        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            columns = GridCells.Fixed(3)
        ) {

            items(movies) { movie ->
                MovieItem(movie)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Column {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null
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
fun MoviesScreenPreview() {
    MoviesScreen(
        movies = List(5) {
            Movie(
                id = 1,
                title = "Movie 1",
                posterUrl = "https://www.cinematerial.com/media/box-office/7967302.jpg",
                rating = 5.0F,
                revenue = 1000000,
                budget = 500000
            )

        }
    )
}