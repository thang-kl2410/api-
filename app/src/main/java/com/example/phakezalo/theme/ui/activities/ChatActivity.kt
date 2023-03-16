package com.example.phakezalo.theme.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.phakezalo.R
import com.example.phakezalo.adapters.MessageAdapter
import com.example.phakezalo.viewModels.MessageViewModel
import com.example.phakezalo.databinding.ActivityChatBinding
import com.example.phakezalo.models.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class ChatActivity : AppCompatActivity() {
    companion object{
        private const val request_code = 1
    }
    private var uri: Uri? = null
    private lateinit var binding: ActivityChatBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var messages:ArrayList<Message>
    private lateinit var adapter: MessageAdapter
    private lateinit var viewModel: MessageViewModel
    private var storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("Friends")
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        binding.nameFrTV.text = intent.getStringExtra("name").toString()
        storage = FirebaseStorage.getInstance()

        binding.imageViewBack.setOnClickListener{
            finish()
        }

        binding.messageET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 1) {
                    setVisibility(true)
                } else {
                    setVisibility(false)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.chatRecycler.setHasFixedSize(true)
        binding.chatRecycler.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = false
        }
        messages = arrayListOf()
        CoroutineScope(Dispatchers.Main).launch {
            getMessages()
        }

        binding.chatRecycler.adapter = MessageAdapter(messages, intent.getStringExtra("avatar").toString(),onClick)

        registerForContextMenu(binding.sendIMG)
        binding.sendIMG.setOnClickListener {
            it.showContextMenu()
        }

        binding.imageIMG.setOnClickListener {
            getImageFromDevice()
        }
    }

    private val onClick:(Message) -> Unit = {
        zoomImage(it)
    }
    private fun zoomImage(image:Message) {
        val intent = Intent(this@ChatActivity, ImageActivity::class.java)
        intent.putExtra("image", image.message)
        startActivity(intent)
    }

    private fun setVisibility(isDisplay:Boolean){
        if(isDisplay){
            binding.optionsIMG.visibility = View.GONE
            binding.micIMG.visibility = View.GONE
            binding.imageIMG.visibility = View.GONE
            binding.sendIMG.visibility = View.VISIBLE
        }else{
            binding.optionsIMG.visibility = View.VISIBLE
            binding.micIMG.visibility = View.VISIBLE
            binding.imageIMG.visibility = View.VISIBLE
            binding.sendIMG.visibility = View.GONE
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_send,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.send_context ->{
                sendMessage(true)
            }
            R.id.receive_context ->{
                sendMessage(false)
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun getImageFromDevice(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "get picture"), request_code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_code && resultCode == Activity.RESULT_OK){
            if(data == null) return
            uri = data.data
            binding.imageContainer.visibility = View.VISIBLE
            Glide.with(this).load(uri).into(binding.imageSend)
            setVisibility(true)
            binding.messageET.visibility = View.GONE
            binding.removeImage.setOnClickListener {
                removeImage()
            }
        }
    }

    private fun removeImage() {
        binding.imageContainer.visibility = View.GONE
        setVisibility(false)
        binding.messageET.visibility = View.VISIBLE
    }

    private fun getMessages() {
        viewModel.getMessages(intent.getStringExtra("id").toString()) { messages ->
            adapter = MessageAdapter(messages, intent.getStringExtra("avatar").toString(),onClick)
            binding.chatRecycler.adapter = adapter
        }
    }

    private fun sendImage(isSend: Boolean){
        if (uri == null) {
            return
        }
        //tải lên hình ảnh vào Firebase Storage
        val storageRef = storage.getReference("Images").child("${System.currentTimeMillis()}")
        storageRef.putFile(uri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUri = uri.toString()
                    val date = Date()
                    val dateFormat = SimpleDateFormat("HH:mm")
                    val time = dateFormat.format(date)
                    viewModel.sendMessage(isSend, imageUri, intent.getStringExtra("id").toString(),time,"image")
                    binding.messageET.text.clear()
                }
            }
        removeImage()
    }

    private fun sendText(isSend: Boolean){
        val message = binding.messageET.text.toString()
        val date = Date()
        val dateFormat = SimpleDateFormat("HH:mm")
        val time = dateFormat.format(date)

        viewModel.sendMessage(isSend, message, intent.getStringExtra("id").toString(),time,"text")
        binding.messageET.text.clear()
    }

    private fun sendMessage(isSend:Boolean) {
        if(binding.messageET.visibility == View.VISIBLE){
            sendText(isSend)
        } else {
            sendImage(isSend)
        }
    }
}