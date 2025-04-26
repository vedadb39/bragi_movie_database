package com.bragi.features.movies.presentation.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.bragi.features.movies.presentation.filter.model.FiltersUiState
import com.bragi.features.movies.presentation.filter.model.GenreUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FilterScreen(
    selectedGenre: GenreUi,
    viewModel: FiltersViewModel = koinViewModel(parameters = { parametersOf(selectedGenre) }),
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
                selectedGenre = uiState.selectedGenre,
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
    selectedGenre: GenreUi,
    onGenreClicked: (GenreUi) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            "Please select a genre",
            style = MaterialTheme.typography.titleLarge,
        )
        FlowRow(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Top
        ) {
            repeat(genres.size) { index ->
                GenreItem(
                    genre = genres[index],
                    selectedGenre = selectedGenre,
                    onGenreClicked = { onGenreClicked(it) })
            }
        }
    }
}

@Composable
private fun GenreItem(
    genre: GenreUi,
    selectedGenre: GenreUi,
    onGenreClicked: (GenreUi) -> Unit = {}
) {
    if (selectedGenre == genre) {
        Button(
            onClick = { onGenreClicked(genre) }
        ) {
            Text(genre.name)
        }
    } else {
        OutlinedButton(
            onClick = { onGenreClicked(genre) }
        ) {
            Text(genre.name)
        }
    }
}

@Preview
@Composable
fun FilterScreenPreview() {
    Content(
        uiState = FiltersUiState(
            isLoading = false,
            genres = listOf(
                GenreUi.All,
                GenreUi.Individual(1, "Action"),
                GenreUi.Individual(2, "Comedy"),
                GenreUi.Individual(3, "Drama")
            ),
            selectedGenre = GenreUi.All
        ),
        onGenreClicked = {})
}