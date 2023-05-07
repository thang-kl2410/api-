package com.example.phakezalo.di

import com.example.phakezalo.ui.activities.ChatActivity
import com.example.phakezalo.ui.activities.MyCloudActivity
import com.example.phakezalo.ui.activities.SearchActivity
import com.example.phakezalo.ui.fragments.FriendFragment
import com.example.phakezalo.viewModels.MessageViewModel
import dagger.Component

@Component(modules = [MessageModule::class])
interface MessageComponent {
    fun getMessageViewModel():MessageViewModel

    fun inject(activity:ChatActivity)
    fun inject(fragment:FriendFragment)
    fun inject(activity:MyCloudActivity)
    fun inject(activity:SearchActivity)
}