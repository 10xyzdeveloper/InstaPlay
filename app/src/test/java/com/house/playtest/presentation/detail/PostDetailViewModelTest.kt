package com.house.playtest.presentation.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.house.playtest.domain.model.Post
import com.house.playtest.domain.usecase.GetPostByIdUseCase
import com.house.playtest.domain.usecase.ToggleLikeUseCase
import com.house.playtest.presentation.navigation.POST_ID_ARG
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {
    
    private lateinit var viewModel: PostDetailViewModel
    private lateinit var getPostByIdUseCase: GetPostByIdUseCase
    private lateinit var toggleLikeUseCase: ToggleLikeUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    
    private val testDispatcher = StandardTestDispatcher()
    
    private val testPostId = "post_123"
    private val mockPost = Post(
        id = testPostId,
        imageUrl = "https://example.com/image.jpg",
        caption = "Test caption",
        authorName = "Test Author",
        likeCount = 100,
        isLiked = false,
        timestamp = System.currentTimeMillis()
    )
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPostByIdUseCase = mockk()
        toggleLikeUseCase = mockk()
        savedStateHandle = SavedStateHandle(mapOf(POST_ID_ARG to testPostId))
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state is Loading then Success when post is found`() = runTest {
        coEvery { getPostByIdUseCase.invoke(testPostId) } returns mockPost
        
        viewModel = PostDetailViewModel(getPostByIdUseCase, toggleLikeUseCase, savedStateHandle)
        
        viewModel.uiState.test {
            // Should start with Loading
            assertEquals(PostDetailUiState.Loading, awaitItem())
            
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Should transition to Success with post
            val successState = awaitItem()
            assert(successState is PostDetailUiState.Success)
            assertEquals(mockPost, (successState as PostDetailUiState.Success).post)
        }
    }
    
    @Test
    fun `initial state is Loading then Error when post is not found`() = runTest {
        coEvery { getPostByIdUseCase.invoke(testPostId) } returns null
        
        viewModel = PostDetailViewModel(getPostByIdUseCase, toggleLikeUseCase, savedStateHandle)
        
        viewModel.uiState.test {
            assertEquals(PostDetailUiState.Loading, awaitItem())
            
            testDispatcher.scheduler.advanceUntilIdle()
            
            val errorState = awaitItem()
            assert(errorState is PostDetailUiState.Error)
            assertEquals("Post not found", (errorState as PostDetailUiState.Error).message)
        }
    }
    
    @Test
    fun `toggleLike updates post state on success`() = runTest {
        val likedPost = mockPost.copy(isLiked = true, likeCount = 101)
        
        coEvery { getPostByIdUseCase.invoke(testPostId) } returns mockPost
        coEvery { toggleLikeUseCase.invoke(testPostId) } returns Result.success(likedPost)
        
        viewModel = PostDetailViewModel(getPostByIdUseCase, toggleLikeUseCase, savedStateHandle)
        
        viewModel.uiState.test {
            // Skip Loading state
            assertEquals(PostDetailUiState.Loading, awaitItem())
            
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Skip initial Success state
            val initialSuccess = awaitItem()
            assert(initialSuccess is PostDetailUiState.Success)
            
            // Toggle like
            viewModel.toggleLike()
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Should update to new state
            val updatedSuccess = awaitItem()
            assert(updatedSuccess is PostDetailUiState.Success)
            assertEquals(likedPost, (updatedSuccess as PostDetailUiState.Success).post)
        }
        
        coVerify { toggleLikeUseCase.invoke(testPostId) }
    }
    
    @Test
    fun `retry calls getPostById again`() = runTest {
        coEvery { getPostByIdUseCase.invoke(testPostId) } returns null andThen mockPost
        
        viewModel = PostDetailViewModel(getPostByIdUseCase, toggleLikeUseCase, savedStateHandle)
        
        viewModel.uiState.test {
            assertEquals(PostDetailUiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            
            // First attempt fails
            val errorState = awaitItem()
            assert(errorState is PostDetailUiState.Error)
            
            // Retry
            viewModel.retry()
            
            // Should show Loading again
            assertEquals(PostDetailUiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Should succeed this time
            val successState = awaitItem()
            assert(successState is PostDetailUiState.Success)
            assertEquals(mockPost, (successState as PostDetailUiState.Success).post)
        }
        
        coVerify(exactly = 2) { getPostByIdUseCase.invoke(testPostId) }
    }
}

