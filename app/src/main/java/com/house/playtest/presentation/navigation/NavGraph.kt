package com.house.playtest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.house.playtest.presentation.detail.PostDetailScreen
import com.house.playtest.presentation.feed.FeedScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Feed.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Feed.route) {
            FeedScreen(
                onPostClick = { postId ->
                    navController.navigate(Screen.PostDetail.createRoute(postId))
                }
            )
        }
        
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(
                navArgument(POST_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            PostDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

