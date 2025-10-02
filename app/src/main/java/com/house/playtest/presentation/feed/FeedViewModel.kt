package com.house.playtest.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.house.playtest.domain.model.Post
import com.house.playtest.domain.usecase.GetPostsUseCase
import com.house.playtest.domain.usecase.ToggleLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()
    
    val postsFlow: Flow<PagingData<Post>> = getPostsUseCase()
        .cachedIn(viewModelScope)
    
    init {
        _uiState.value = FeedUiState.Success
    }
    
    fun toggleLike(postId: String) {
        viewModelScope.launch {
            toggleLikeUseCase(postId)
                .onSuccess {
                    // Paging 3 will automatically refresh the data
                }
                .onFailure { error ->
                    _uiState.value = FeedUiState.Error(error.message ?: "Failed to toggle like")
                    // Reset to success after showing error
                    _uiState.value = FeedUiState.Success
                }
        }
    }
}

