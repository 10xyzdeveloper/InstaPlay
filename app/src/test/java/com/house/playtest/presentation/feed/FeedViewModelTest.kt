package com.house.playtest.presentation.feed

import androidx.paging.PagingData
import app.cash.turbine.test
import com.house.playtest.domain.model.Post
import com.house.playtest.domain.usecase.GetPostsUseCase
import com.house.playtest.domain.usecase.ToggleLikeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {
    
    private lateinit var viewModel: FeedViewModel
    private lateinit var getPostsUseCase: GetPostsUseCase
    private lateinit var toggleLikeUseCase: ToggleLikeUseCase
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPostsUseCase = mockk()
        toggleLikeUseCase = mockk()
        
        // Mock GetPostsUseCase to return an empty PagingData flow
        coEvery { getPostsUseCase.invoke() } returns flowOf(PagingData.empty())
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state is Success`() = runTest {
        viewModel = FeedViewModel(getPostsUseCase, toggleLikeUseCase)
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(FeedUiState.Success, state)
        }
    }
    
    @Test
    fun `toggleLike calls use case with correct postId`() = runTest {
        val postId = "post_123"
        val mockPost = Post(
            id = postId,
            imageUrl = "https://example.com/image.jpg",
            caption = "Test caption",
            authorName = "Test Author",
            likeCount = 100,
            isLiked = false,
            timestamp = System.currentTimeMillis()
        )
        
        coEvery { toggleLikeUseCase.invoke(postId) } returns Result.success(mockPost.copy(isLiked = true, likeCount = 101))
        
        viewModel = FeedViewModel(getPostsUseCase, toggleLikeUseCase)
        
        viewModel.toggleLike(postId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify { toggleLikeUseCase.invoke(postId) }
    }
    
    @Test
    fun `toggleLike failure shows error state`() = runTest {
        val postId = "post_123"
        val errorMessage = "Failed to toggle like"
        
        coEvery { toggleLikeUseCase.invoke(postId) } returns Result.failure(Exception(errorMessage))
        
        viewModel = FeedViewModel(getPostsUseCase, toggleLikeUseCase)
        
        viewModel.uiState.test {
            // Skip initial Success state
            assertEquals(FeedUiState.Success, awaitItem())
            
            viewModel.toggleLike(postId)
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Should show error
            val errorState = awaitItem()
            assert(errorState is FeedUiState.Error)
            assertEquals(errorMessage, (errorState as FeedUiState.Error).message)
            
            // Should reset to success
            val successState = awaitItem()
            assertEquals(FeedUiState.Success, successState)
        }
    }
}

