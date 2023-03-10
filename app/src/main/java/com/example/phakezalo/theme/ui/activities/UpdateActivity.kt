package com.example.phakezalo.theme.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.phakezalo.database.viewModels.FriendViewModel
import com.example.phakezalo.databinding.ActivityUpdateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity() {
    companion object{
        private val request_code = 1
    }

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var viewModel:FriendViewModel
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FriendViewModel::class.java]
        setData()
    }

    private fun setData(){
        Glide.with(this).load(intent.getStringExtra("avatar").toString()).into(binding.avatar)
        binding.apply {
            imageViewBack.setOnClickListener {
                finish()
            }
            nameUpdateET.setText(intent.getStringExtra("name").toString())
            updateBTN.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val id = intent.getStringExtra("id").toString()
                    viewModel.updateDataFriend(id, binding.nameUpdateET.text.toString(), uri!!)
                    finish()
                }
            }
            avatar.setOnClickListener {
                getImageFromDevice()
            }
        }
    }

    private fun getImageFromDevice(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "get picture"),
            UpdateActivity.request_code
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UpdateActivity.request_code && resultCode == Activity.RESULT_OK){
            if(data == null) return
            uri = data.data
            Glide.with(this).load(uri).into(binding.avatar)
        }
    }
}