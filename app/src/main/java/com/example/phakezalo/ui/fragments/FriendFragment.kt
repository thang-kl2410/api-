package com.example.phakezalo.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phakezalo.R
import com.example.phakezalo.databinding.FragmentFriendsBinding
import com.example.phakezalo.ui.adapters.FriendAdapter
import com.example.phakezalo.viewModels.FriendViewModel
import com.example.phakezalo.models.Friend
import com.example.phakezalo.ui.activities.AddFriendActivity
import com.example.phakezalo.ui.activities.ChatActivity
import com.example.phakezalo.ui.activities.SearchActivity
import com.example.phakezalo.ui.activities.UpdateActivity
import com.example.phakezalo.viewModels.MessageViewModel
import javax.inject.Inject
import com.example.phakezalo.di.DaggerFriendComponent
import com.example.phakezalo.di.DaggerMessageComponent

class FriendFragment : Fragment(R.layout.fragment_friends) {
    private lateinit var binding:FragmentFriendsBinding
    private lateinit var adapter: FriendAdapter
    private var friends:ArrayList<Friend> = arrayListOf()

    @Inject
    lateinit var viewModel: FriendViewModel

    @Inject
    lateinit var messageViewModel:MessageViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFriendsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val component = DaggerFriendComponent.create()
        component.inject(this@FriendFragment)

        val messageComponent = DaggerMessageComponent.create()
        messageComponent.inject(this@FriendFragment)


        binding.apply {
            imageViewAdd.setOnClickListener {
            startActivity(Intent(requireActivity(), AddFriendActivity::class.java))
        }

            searchTV.setOnClickListener {
                startActivity(Intent(requireActivity(), SearchActivity::class.java))
            }

            chatRecycler.setHasFixedSize(true)
            chatRecycler.layoutManager = LinearLayoutManager(requireContext())

            registerForContextMenu(chatRecycler)
        }
        getFriends()
    }

    private fun getFriends() {
        viewModel.isLoading().observe(requireActivity()) {
            if (it) {
                binding.isLoading.visibility = View.VISIBLE
            } else {
                binding.isLoading.visibility = View.GONE
            }
        }
        viewModel.listFriend.observe(requireActivity()) {
            friends = it as ArrayList<Friend>
            adapter = FriendAdapter(friends, messageViewModel ,onClick)
            binding.chatRecycler.adapter = adapter
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
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("Confirm")
                builder.setMessage("Bạn có muốn xóa?")
                builder.setPositiveButton("YES"){ _, _ ->
                    viewModel.changeStateDelete(friends[adapter.selectedItemPosition].id.toString(), true)
                    Toast.makeText(requireActivity(),"Xóa bạn bè thành công.",Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("NO"){ _, _ ->
                }
                val dialog = builder.create()
                dialog.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}