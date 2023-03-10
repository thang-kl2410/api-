package com.example.phakezalo.theme.ui.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phakezalo.OnClick
import com.example.phakezalo.R
import com.example.phakezalo.theme.ui.activities.AddFriendActivity
import com.example.phakezalo.adapters.FriendAdapters
import com.example.phakezalo.theme.ui.activities.ChatActivity
import com.example.phakezalo.database.viewModels.FriendViewModel
import com.example.phakezalo.models.Friend
import com.example.phakezalo.theme.ui.activities.UpdateActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendFragment : Fragment(R.layout.fragment_messages) {
    private lateinit var dbRef:DatabaseReference
    private lateinit var addImg:ImageView
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:FriendAdapters
    private var friends:ArrayList<Friend> = arrayListOf()

    private val viewModel: FriendViewModel by lazy {
        ViewModelProvider(requireActivity())[FriendViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myThread = Thread.currentThread()
        Log.i("Thread is running on: ", "onCreate Function thread name: ${myThread.name}")

        addImg= view.findViewById(R.id.imageViewAdd)
        recyclerView = view.findViewById(R.id.chatRecycler)

        dbRef = FirebaseDatabase.getInstance().getReference("Friends")

        addImg.setOnClickListener {
            startActivity(Intent(requireActivity(), AddFriendActivity::class.java))
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        registerForContextMenu(recyclerView)

        CoroutineScope(Dispatchers.Main).launch {
            getFriends()
        }

    }

    private fun getFriends() {
        val progressBar = ProgressDialog(requireContext())
        progressBar.setMessage("Loading...")
        progressBar.show()

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.listFriend.observe(requireActivity()) {
                val myThread = Thread.currentThread()
                Log.i("Thread is running on: ", "getFriend Function thread name: ${myThread.name}")
                friends = it as ArrayList<Friend>
                adapter = FriendAdapters(friends, object : OnClick {
                    override fun setOnClickListener(position: Int) {
                        val intent = Intent(requireContext(), ChatActivity::class.java)
                        intent.putExtra("name", it[position].name)
                        intent.putExtra("id", it[position].id)
                        intent.putExtra("avatar",it[position].avatar)
                        startActivity(intent)
                    }
                })
                recyclerView.adapter = adapter

                progressBar.dismiss() // ẩn ProgressBar khi lấy xong danh sách bạn bè
            }
        }
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