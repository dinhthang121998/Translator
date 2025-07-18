package com.example.translator.presentation.navigators.di

import com.example.navigation.NavigateFeedback
import com.example.navigation.NavigateTheme
import com.example.translator.presentation.navigators.NavigateFeedbackImpl
import com.example.translator.presentation.navigators.NavigateThemeImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorsModule {

    @Binds
    @Singleton
    abstract fun bindNavigateFeedback(
        navigateFeedbackImpl: NavigateFeedbackImpl
    ): NavigateFeedback

    @Binds
    @Singleton
    abstract fun bindNavigateTheme(
        navigateThemeImpl: NavigateThemeImpl
    ): NavigateTheme
}