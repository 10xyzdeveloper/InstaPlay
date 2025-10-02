package com.house.playtest.presentation.detail

import com.house.playtest.domain.model.Post

sealed class PostDetailUiState {
    data object Loading : PostDetailUiState()
    data class Success(val post: Post) : PostDetailUiState()
    data class Error(val message: String) : PostDetailUiState()
}

