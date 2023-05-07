package com.example.phakezalo.di

import com.example.phakezalo.database.MessageRepositoryImpl
import com.example.phakezalo.database.repository.MessageRepository
import dagger.Module
import dagger.Provides

@Module
object MessageModule {
    @Provides
    fun provideFriendRepository(): MessageRepository {
        return MessageRepositoryImpl()
    }
}