package com.house.playtest.presentation.navigation

const val POST_ID_ARG = "postId"

sealed class Screen(val route: String) {
    data object Feed : Screen("feed")
    data object PostDetail : Screen("post_detail/{$POST_ID_ARG}") {
        fun createRoute(postId: String) = "post_detail/$postId"
    }
}

