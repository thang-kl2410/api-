package com.example.phakezalo.theme.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phakezalo.R
import com.example.phakezalo.adapters.FriendAdapter
import com.example.phakezalo.viewModels.FriendViewModel
import com.example.phakezalo.models.Friend
import com.example.phakezalo.models.Message
import com.example.phakezalo.theme.ui.activities.AddFriendActivity
import com.example.phakezalo.theme.ui.activities.ChatActivity
import com.example.phakezalo.theme.ui.activities.SearchActivity
import com.example.phakezalo.theme.ui.activities.UpdateActivity
import com.example.phakezalo.viewModels.MessageViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FriendFragment : Fragment(R.layout.fragment_messages) {
    private lateinit var addImg:ImageView
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:FriendAdapter
    private lateinit var search:TextView
    private lateinit var isLoading:ProgressBar
    private var friends:ArrayList<Friend> = arrayListOf()

    private val viewModel: FriendViewModel by lazy {
        ViewModelProvider(requireActivity())[FriendViewModel::class.java]
    }

    private val messageViewModel:MessageViewModel by lazy {
        ViewModelProvider(requireActivity())[MessageViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addImg= view.findViewById(R.id.imageViewAdd)
        recyclerView = view.findViewById(R.id.chatRecycler)
        isLoading = view.findViewById(R.id.isLoading)
        search = view.findViewById(R.id.searchTV)

        addImg.setOnClickListener {
            startActivity(Intent(requireActivity(), AddFriendActivity::class.java))
        }

        search.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        registerForContextMenu(recyclerView)
        getFriends()

    }

    private fun getFriends() {
        viewModel.isLoading().observe(requireActivity()) {
            if (it) {
                isLoading.visibility = View.VISIBLE
            } else {
                isLoading.visibility = View.GONE
            }
        }
        viewModel.listFriend.observe(requireActivity()) {
            val myThread = Thread.currentThread()
            Log.i("Thread is running on: ", "getFriend Function thread name: ${myThread.name}")
            friends = it as ArrayList<Friend>
            adapter = FriendAdapter(friends, messageViewModel ,onClick)
            recyclerView.adapter = adapter
        }
    }

    private val onClick:(Friend) -> Unit = {
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("name", it.name)
        intent.putExtra("id", it.id)
        intent.putExtra("avatar",it.avatar)
        messageViewModel.getEndMessage(intent.putExtra("id", it.id).toString()) { endMessage ->
            Toast.makeText(requireActivity(), endMessage.message.toString(), Toast.LENGTH_SHORT).show()
        }
        startActivity(intent)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.update_context -> {
                val i = Intent(requireActivity(), UpdateActivity::class.java)
                i.putExtra("id", friends[adapter.selectedItemPosition].id.toString())
                i.putExtra("name", friends[adapter.selectedItemPosition].name.toString())
                i.putExtra("avatar", friends[adapter.selectedItemPosition].avatar.toString())
                startActivity(i)
                true
            }
            R.id.delete_context -> {
                viewModel.deleteFriend(friends[adapter.selectedItemPosition].id.toString())
                Toast.makeText(requireActivity(),"Xóa bạn bè thành công.",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}