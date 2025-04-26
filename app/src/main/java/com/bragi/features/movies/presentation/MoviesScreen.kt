package com.bragi.features.movies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.bragi.R
import com.bragi.features.movies.domain.model.Movie
import com.bragi.features.movies.domain.model.PosterImage
import com.bragi.features.movies.presentation.filter.model.GenreUi
import com.bragi.features.movies.presentation.model.MoviesUiState
import com.bragi.features.movies.presentation.util.formatMoney
import com.bragi.features.movies.presentation.util.round
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MoviesScreen(
    selectedGenre: GenreUi,
    viewModel: MoviesViewModel = koinViewModel(parameters = { parametersOf(selectedGenre) }),
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
        modifier = modifier.padding(vertical = 16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // I didn't have time to add pagination since Paging 3 library isn't quite good and it is confusing and I didn't have time to implement it
        // Also I think they solution violates clean architecture since data models leaks in the presentation
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
private fun MovieItem(movie: Movie) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            when (movie.posterImage) {
                PosterImage.Unavailable -> {
                    Image(
                        painter = painterResource(id = R.drawable.movie_placeholder),
                        contentDescription = null
                    )
                }

                is PosterImage.Url -> {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        model = movie.posterImage.url,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.movie_placeholder)
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    maxLines = 1,
                    text = "Rating: ${movie.rating.round(1)}",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    maxLines = 1,
                    text = "Revenue: ${movie.revenue.formatMoney()}",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    maxLines = 1,
                    text = "Budget: ${movie.budget.formatMoney()}",
                    style = MaterialTheme.typography.labelLarge
                )
            }

        }
    }
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
                    posterImage = PosterImage.Url(url = ""),
                    rating = 5.0F,
                    revenue = 1000000,
                    budget = 500000
                )
            }
        )
    )
}
