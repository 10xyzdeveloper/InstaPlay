package com.house.playtest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.house.playtest.domain.model.Post
import com.house.playtest.domain.repository.PostRepository

class PostPagingSource(
    private val repository: PostRepository
) : PagingSource<Int, Post>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val currentPage = params.key ?: 0
            val posts = repository.getPosts(
                page = currentPage,
                pageSize = params.loadSize
            )
            
            LoadResult.Page(
                data = posts,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (posts.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
