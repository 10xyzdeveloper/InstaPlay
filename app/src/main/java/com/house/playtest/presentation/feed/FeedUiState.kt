package com.house.playtest.presentation.feed

sealed class FeedUiState {
    data object Loading : FeedUiState()
    data object Success : FeedUiState()
    data class Error(val message: String) : FeedUiState()
}

