package com.prestigerito.sweetnothing.di

import com.prestigerito.sweetnothing.domain.ScoreDataSource

expect class AppModule {
    val scoreDataSource: ScoreDataSource
}