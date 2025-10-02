package com.house.playtest.di

import com.house.playtest.data.repository.PostRepositoryImpl
import com.house.playtest.data.source.MockPostDataSource
import com.house.playtest.data.source.PostDataSource
import com.house.playtest.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindPostDataSource(
        mockPostDataSource: MockPostDataSource
    ): PostDataSource
    
    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository
}

