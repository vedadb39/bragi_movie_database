package com.bragi.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.bragi.movies.domain.model.GenreState
import com.bragi.movies.presentation.FilterScreenRoot
import com.bragi.movies.presentation.MoviesScreenRoot
import com.bragi.movies.presentation.Routes
import com.bragi.movies.presentation.model.CustomNavType
import com.bragi.movies.presentation.model.GenreStateUiModel
import kotlin.reflect.typeOf

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = Routes.Movies(GenreStateUiModel.All)) {
            composable<Routes.Movies>(
                typeMap = mapOf(
                    typeOf<GenreStateUiModel>() to CustomNavType.GenreState
                )
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<Routes.Movies>()
                MoviesScreenRoot(
                    selectedGenre = arguments.genre,
                    onFilterClick = {
                        navController.navigate(Routes.Filter)
                    }
                )
            }
            composable<Routes.Filter> {
                FilterScreenRoot(
                    onGenreClicked = { genre ->
                        navController.navigate(Routes.Movies(genre))

                    }
                )
            }
        }
    }

    NavHost(navController = navController, graph = navGraph)
}