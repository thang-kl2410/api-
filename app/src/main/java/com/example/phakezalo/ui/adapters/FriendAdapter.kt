package com.example.phakezalo.ui.adapters

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.phakezalo.R
import com.example.phakezalo.models.Friend
import com.example.phakezalo.viewModels.MessageViewModel
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter(private var listFriend:List<Friend>,
                    private var messageViewModel: MessageViewModel,
                     private val onClick:(Friend) -> Unit
                     ): RecyclerView.Adapter<FriendAdapter.Holder>() {
    var selectedItemPosition: Int = -1

    inner class Holder(item: View):ViewHolder(item), View.OnCreateContextMenuListener {
        val avatar:CircleImageView = item.findViewById(R.id.avatar)
        val nameTV:TextView = item.findViewById(R.id.nameTV)
        val messageLatest:TextView = item.findViewById(R.id.endMssTV)
        val textTime:TextView = item.findViewById(R.id.textTime)

        init {
            item.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.context_friend, menu)

            // Lưu lại vị trí của item được chọn
            selectedItemPosition = bindingAdapterPosition
            v.isSelected = true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        view.id = R.id.friend_item
        return Holder(view)
    }

    override fun getItemCount(): Int = listFriend.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.itemView.context).load(listFriend[position].avatar).into(holder.avatar)
        holder.nameTV.text = listFriend[position].name

        try{
            messageViewModel.getEndMessage(listFriend[position].id.toString()) { endMessage ->
                holder.textTime.text = endMessage.time.toString()
                if (endMessage.type == "text") {
                    holder.messageLatest.text = endMessage.message.toString()
                } else if (endMessage.type == "image") {
                    holder.messageLatest.text = "[Hình ảnh]"
                }
            }
        } catch (e:Exception){
            Log.i("Firebase: ", e.toString())
        } finally {
            holder.messageLatest.text = ""
            holder.textTime.text = ""
        }

        holder.itemView.setOnClickListener {
            onClick(listFriend[position])
        }
    }

    fun setData(data: List<Friend>) {
        this.listFriend = data
    }
}