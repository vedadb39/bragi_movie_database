package com.bragi.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.bragi.features.movies.presentation.filter.FilterScreen
import com.bragi.features.movies.presentation.MoviesScreen
import com.bragi.features.movies.presentation.Routes
import com.bragi.features.movies.presentation.model.CustomNavType
import com.bragi.features.movies.presentation.model.GenreUi

import kotlin.reflect.typeOf

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = Routes.Movies(GenreUi.All)) {
            composable<Routes.Movies>(
                typeMap = mapOf(
                    typeOf<GenreUi>() to CustomNavType.Genre
                )
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<Routes.Movies>()
                MoviesScreen(
                    selectedGenre = arguments.genre,
                    onFilterClick = {
                        navController.navigate(Routes.Filter(arguments.genre))
                    }
                )
            }
            composable<Routes.Filter>(
                typeMap = mapOf(
                    typeOf<GenreUi>() to CustomNavType.Genre
                )
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<Routes.Movies>()

                FilterScreen(
                    selectedGenre = arguments.genre,
                    onGenreClicked = { genre ->
                        navController.navigate(Routes.Movies(genre))

                    }
                )
            }
        }
    }

    NavHost(navController = navController, graph = navGraph)
}