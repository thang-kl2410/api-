package com.example.phakezalo.di

import com.example.phakezalo.ui.activities.AddFriendActivity
import com.example.phakezalo.ui.activities.MyCloudActivity
import com.example.phakezalo.ui.activities.SearchActivity
import com.example.phakezalo.ui.activities.UpdateActivity
import com.example.phakezalo.ui.fragments.FriendFragment
import com.example.phakezalo.viewModels.FriendViewModel
import dagger.Component

@Component(modules = [FriendModule::class])
interface FriendComponent {
    fun getFriendViewModel():FriendViewModel

    fun inject(fragment: FriendFragment)
    fun inject(activity: AddFriendActivity)
    fun inject(activity: UpdateActivity)
    fun inject(activity: SearchActivity)
    fun inject(activity: MyCloudActivity)
}