package com.example.phakezalo.di

import com.example.phakezalo.database.FriendRepositoryImpl
import com.example.phakezalo.database.repository.FriendRepository
import dagger.Module
import dagger.Provides

@Module
object FriendModule {
    @Provides
    fun provideFriendRepository(): FriendRepository {
        return FriendRepositoryImpl()
    }
}