package com.example.phakezalo.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.phakezalo.viewModels.FriendViewModel
import com.example.phakezalo.databinding.ActivityAddFriendBinding
import javax.inject.Inject
import com.example.phakezalo.di.DaggerFriendComponent

class AddFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFriendBinding

    @Inject
    lateinit var viewModel: FriendViewModel


    private lateinit var dialog:ProgressDialog

    private var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val component = DaggerFriendComponent.create()
        component.inject(this@AddFriendActivity)

        binding.apply {
            avatarImg.setOnClickListener {
                getImageFromDevice()
            }

            addFriendBTN.setOnClickListener {
                dialog = ProgressDialog(this@AddFriendActivity)
                viewModel.isLoading().observe(this@AddFriendActivity) {
                    if (it) {
                        dialog.show()
                    } else {
                        dialog.dismiss()
                    }
                }
                insertFriend()
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
        startActivityForResult(Intent.createChooser(intent, "get picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            if(data == null) return
            uri = data.data
            Glide.with(this).load(uri).into(binding.avatarImg)
        }
    }

    private fun insertFriend(){
        if(uri != null){
            try{
                viewModel.insertFriend(binding.friendNameET.text.toString(), uri!!)
            } catch (e:Exception){
                Log.i("Error when inserting:", e.toString())
            }
            finish()
        } else {
            Toast.makeText(this@AddFriendActivity, "Bạn chưa chọn hình ảnh làm avatar", Toast.LENGTH_SHORT).show()
        }
    }
}