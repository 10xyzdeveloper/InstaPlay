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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getPostsUseCase: GetPostsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase
) : ViewModel() {
    
    // Paging 3 handles the post list state (loading, error, data)
    val postsFlow: Flow<PagingData<Post>> = getPostsUseCase()
        .cachedIn(viewModelScope)
    
    // UI state handles screen-level state (snackbars, filters, etc.)
    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()
    
    fun toggleLike(postId: String) {
        viewModelScope.launch {
            toggleLikeUseCase(postId)
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(snackbarMessage = error.message ?: "Failed to toggle like")
                    }
                }
        }
    }
    
    fun snackbarMessageShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
    
    // Future functions can be easily added:
    // fun applyFilter(filter: FilterType) { ... }
    // fun updateSearchQuery(query: String) { ... }
    // fun changeSortOrder(order: SortOrder) { ... }
}

