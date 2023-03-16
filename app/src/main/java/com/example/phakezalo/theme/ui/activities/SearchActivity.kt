package com.example.phakezalo.theme.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phakezalo.adapters.FriendAdapter
import com.example.phakezalo.databinding.ActivitySearchBinding
import com.example.phakezalo.models.Friend
import com.example.phakezalo.viewModels.FriendViewModel
import com.example.phakezalo.viewModels.MessageViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    private lateinit var isLoading: ProgressBar
    private var friends:ArrayList<Friend> = arrayListOf()
    private lateinit var adapter:FriendAdapter
    // Khai báo biến filteredData chứa kết quả tìm kiếm
    val filteredData: MutableList<Friend> = mutableListOf()
    private val viewModel: FriendViewModel by lazy {
        ViewModelProvider(this@SearchActivity)[FriendViewModel::class.java]
    }

    private val messageViewModel: MessageViewModel by lazy {
        ViewModelProvider(this@SearchActivity)[MessageViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIMG.setOnClickListener {
            finish()
        }

        binding.friendSearchRe.setHasFixedSize(true)
        binding.friendSearchRe.layoutManager = LinearLayoutManager(this@SearchActivity)
        getFriends()

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredData.clear()
                for (item in friends) {
                    if (item.name!!.toLowerCase().contains(s.toString())) {
                        filteredData.add(item)
                    }
                }
                adapter.setData(filteredData)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun getFriends() {
        viewModel.isLoading().observe(this@SearchActivity) {
            if (it) {
                binding.isLoading.visibility = View.VISIBLE
            } else {
                binding.isLoading.visibility = View.GONE
            }
        }
        viewModel.listFriend.observe(this@SearchActivity) {
            friends = it as ArrayList<Friend>
            adapter = FriendAdapter(friends, messageViewModel ,onClick)
            binding.friendSearchRe.adapter = adapter
        }
    }

    private val onClick:(Friend) -> Unit = {
        val intent = Intent(this@SearchActivity, ChatActivity::class.java)
        intent.putExtra("name", it.name)
        intent.putExtra("id", it.id)
        intent.putExtra("avatar",it.avatar)
        messageViewModel.getEndMessage(intent.putExtra("id", it.id).toString()) { endMessage ->
            Toast.makeText(this@SearchActivity, endMessage.message.toString(), Toast.LENGTH_SHORT).show()
        }
        startActivity(intent)
    }

}