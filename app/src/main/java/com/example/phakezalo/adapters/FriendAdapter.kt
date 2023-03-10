package com.example.phakezalo.adapters

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.phakezalo.OnClick
import com.example.phakezalo.R
import com.example.phakezalo.models.Friend
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapters(private var listFriend:List<Friend>, private val onClick:OnClick): RecyclerView.Adapter<FriendAdapters.Holder>() {
    var selectedItemPosition: Int = -1

    inner class Holder(item: View):ViewHolder(item), View.OnCreateContextMenuListener {
        val avatar:CircleImageView = item.findViewById(R.id.avatar)
        val nameTV:TextView = item.findViewById(R.id.nameTV)

        init {
            item.setOnCreateContextMenuListener(this)
            item.setOnClickListener {
                onClick.setOnClickListener(adapterPosition)
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.context_friend, menu)

            // Lưu lại vị trí của item được chọn và cập nhật lại giao diện
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

        holder.itemView.setOnClickListener {
            onClick.setOnClickListener(position)
        }
    }
}