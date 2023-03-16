package com.example.phakezalo.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phakezalo.R
import com.example.phakezalo.models.Message
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(private val messages:List<Message>, private val avatar:String, private val onClick:(Message) -> Unit): RecyclerView.Adapter<MessageAdapter.Holder>() {
    var selectedItemPosition: Int = -1
    inner class Holder(item:View):RecyclerView.ViewHolder(item){
        val message:TextView = item.findViewById(R.id.messageTV)
        val time:TextView = item.findViewById(R.id.timeSendTV)
        val imageContainer:CardView = item.findViewById(R.id.containerImage)
        val image:ImageView = item.findViewById(R.id.imageMss)
        val avatar:CircleImageView = item.findViewById(R.id.circleImageView)

        init {
            //item.setOnCreateContextMenuListener(this)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view:View = if(viewType == 1){
            LayoutInflater.from(parent.context).inflate(R.layout.message_send, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.message_receive, parent, false)
        }
        return Holder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSend!!) 1 else 2
    }

    override fun getItemCount():Int = messages.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.itemView.context).load(avatar).into(holder.avatar)
        holder.avatar.visibility = View.GONE
        if(position>=0){
            if(messages[position].isSend == false){
                holder.avatar.visibility = View.VISIBLE
                if(messages[position+1].isSend == false){
                    holder.avatar.visibility = View.GONE
                }
            } else{
                holder.avatar.visibility = View.GONE
            }
        }
        if(messages[position].type.toString() == "text"){
            holder.message.visibility = View.VISIBLE
            holder.time.visibility = View.VISIBLE
            holder.imageContainer.visibility = View.GONE
            holder.message.text = messages[position].message
            holder.time.text = messages[position].time
        } else if (messages[position].type.toString() == "image"){
            Glide.with(holder.itemView.context).load(messages[position].message).into(holder.image)
            holder.message.visibility = View.GONE
            holder.time.visibility = View.GONE
            holder.imageContainer.visibility = View.VISIBLE

            holder.imageContainer.setOnClickListener {
                onClick(messages[position])
            }
        }
    }
}