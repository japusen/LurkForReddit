package com.example.lurkforreddit.di

import com.example.lurkforreddit.data.repository.AccessTokenRepositoryImpl
import com.example.lurkforreddit.data.repository.CommentThreadRepositoryImpl
import com.example.lurkforreddit.data.repository.DuplicatePostsRepositoryImpl
import com.example.lurkforreddit.data.repository.PostRepositoryImpl
import com.example.lurkforreddit.data.repository.ProfileRepositoryImpl
import com.example.lurkforreddit.data.repository.SearchResultsRepositoryImpl
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.CommentThreadRepository
import com.example.lurkforreddit.domain.repository.DuplicatePostsRepository
import com.example.lurkforreddit.domain.repository.PostRepository
import com.example.lurkforreddit.domain.repository.ProfileRepository
import com.example.lurkforreddit.domain.repository.SearchResultsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccessTokenRepository(
        accessTokenRepositoryImpl: AccessTokenRepositoryImpl
    ): AccessTokenRepository

    @Binds
    @Singleton
    abstract fun bindCommentThreadRepository(
        commentThreadRepositoryImpl: CommentThreadRepositoryImpl
    ): CommentThreadRepository

    @Binds
    @Singleton
    abstract fun bindDuplicatePostsRepository(
        duplicatePostsRepositoryImpl: DuplicatePostsRepositoryImpl
    ): DuplicatePostsRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindSearchResultsRepository(
        searchResultsRepositoryImpl: SearchResultsRepositoryImpl
    ): SearchResultsRepository
}