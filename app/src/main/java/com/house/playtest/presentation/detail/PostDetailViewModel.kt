package com.house.playtest.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.house.playtest.domain.usecase.GetPostByIdUseCase
import com.house.playtest.domain.usecase.ToggleLikeUseCase
import com.house.playtest.presentation.navigation.POST_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val postId: String = checkNotNull(savedStateHandle[POST_ID_ARG])
    
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadPost()
    }
    
    private fun loadPost() {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            try {
                val post = getPostByIdUseCase(postId)
                if (post != null) {
                    _uiState.value = PostDetailUiState.Success(post)
                } else {
                    _uiState.value = PostDetailUiState.Error("Post not found")
                }
            } catch (e: Exception) {
                _uiState.value = PostDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun toggleLike() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is PostDetailUiState.Success) {
                toggleLikeUseCase(postId)
                    .onSuccess { updatedPost ->
                        _uiState.value = PostDetailUiState.Success(updatedPost)
                    }
                    .onFailure { error ->
                        // Keep current state but could show a snackbar
                    }
            }
        }
    }
    
    fun retry() {
        loadPost()
    }
}

