package com.bragi.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.bragi.movies.presentation.FilterScreenRoot
import com.bragi.movies.presentation.MoviesScreenRoot

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = Routes.Movies) {
            composable<Routes.Movies> {
                MoviesScreenRoot()
            }
            composable<Routes.Filter> {
                FilterScreenRoot()
            }
        }
    }

    NavHost(navController = navController, graph = navGraph)
}