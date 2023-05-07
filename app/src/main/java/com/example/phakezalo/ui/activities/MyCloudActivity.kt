package com.example.phakezalo.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phakezalo.R
import com.example.phakezalo.databinding.ActivityMyCloudBinding
import com.example.phakezalo.di.DaggerFriendComponent
import com.example.phakezalo.di.DaggerMessageComponent
import com.example.phakezalo.models.Friend
import com.example.phakezalo.ui.adapters.AllFriendInCloudAdapter
import com.example.phakezalo.viewModels.FriendViewModel
import com.example.phakezalo.viewModels.MessageViewModel
import javax.inject.Inject

class MyCloudActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCloudBinding
    private var friends:ArrayList<Friend> = arrayListOf()
    private lateinit var adapter: AllFriendInCloudAdapter

    @Inject
    lateinit var viewModel: FriendViewModel
    @Inject
    lateinit var messageViewModel:MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCloudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val component = DaggerFriendComponent.create()
        component.inject(this@MyCloudActivity)
        val messageComponent = DaggerMessageComponent.create()
        messageComponent.inject(this@MyCloudActivity)

        binding.friendRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MyCloudActivity)
            getFriends(true)
        }

        registerForContextMenu(binding.imageViewOption)
        binding.imageViewOption.setOnClickListener {
            it.showContextMenu(0f, 0f)
        }

        binding.imageView.setOnClickListener {
            finish()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v == binding.imageViewOption) {
            menu?.setHeaderTitle("State")
            menu?.add(Menu.NONE, R.id.context_deletedFriends, Menu.NONE, "Đoạn chat đã xóa")
            menu?.add(Menu.NONE, R.id.context_allFriends, Menu.NONE, "Tất cả")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.context_allFriends ->{
                getFriends(true)
            }
            R.id.context_deletedFriends ->{
                getFriends(false)
            }
            R.id.context_delete_cloud -> {
                val builder = AlertDialog.Builder(this@MyCloudActivity)
                builder.setTitle("Confirm")
                builder.setMessage("Bạn có muốn xóa không?")
                builder.setPositiveButton("YES"){ _, _ ->
                    viewModel.deleteFriend(friends[adapter.selectedItemPosition].id.toString())
                    Toast.makeText(this@MyCloudActivity,"Xóa thành công.", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("NO"){ _, _ ->
                }
                val dialog = builder.create()
                dialog.show()
            }
            R.id.context_restore ->{
                viewModel.changeStateDelete(friends[adapter.selectedItemPosition].id.toString(), false)
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun getFriends(state:Boolean) {
        //state: deleted = false, all = true
        if(state){
            viewModel.allFriend.observe(this@MyCloudActivity) {
                friends = it as ArrayList<Friend>
                adapter = AllFriendInCloudAdapter(friends, messageViewModel)
                binding.friendRecycler.adapter = adapter
            }
        } else {
            viewModel.listDeletedFriend.observe(this@MyCloudActivity) {
                friends = it as ArrayList<Friend>
                adapter = AllFriendInCloudAdapter(friends, messageViewModel)
                binding.friendRecycler.adapter = adapter
            }
        }
    }
}