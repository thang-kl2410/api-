package com.example.phakezalo.theme.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.phakezalo.database.viewModels.FriendViewModel
import com.example.phakezalo.databinding.ActivityAddFriendBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFriendActivity : AppCompatActivity() {
    companion object{
        private val request_code = 1
    }
    private lateinit var binding: ActivityAddFriendBinding

    private lateinit var viewModel:FriendViewModel

    private var storage = Firebase.storage
    private var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()

        viewModel = ViewModelProvider(this)[FriendViewModel::class.java]

        binding.apply {
            avatarImg.setOnClickListener {
                getImageFromDevice()
            }

            addFriendBTN.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    insertFriend()
                }
            }

            imageViewBack.setOnClickListener {
                finish()
            }
        }
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
            Glide.with(this).load(uri).into(binding.avatarImg)
        }
    }

    private fun insertFriend(){
        if(uri != null){
            viewModel.insertFriend(binding.friendNameET.text.toString(), uri!!)
            finish()
        } else {
            Toast.makeText(this@AddFriendActivity, "Bạn chưa chọn hình ảnh làm avatar", Toast.LENGTH_SHORT).show()
        }
    }
}