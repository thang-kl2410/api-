package com.example.phakezalo.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.phakezalo.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    val phoneNumber = "0373461555"
    val email = "vthang2420@gmail.com"
    val github = "https://github.com/thang-kl2410"
    val facebook = "https://www.facebook.com/profile.php?id=100017306778563"
    val tiktok = "https://www.tiktok.com/@__mibe"
    val instagram = "https://www.instagram.com/anthan.2410/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.messageContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:$phoneNumber")
            intent.putExtra("sms_body", "")
            startActivity(intent)
        }

        binding.callContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        binding.emailContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            startActivity(intent)
        }

        binding.facebookContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(facebook)
            startActivity(intent)
        }
        binding.instagramContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(instagram)
            startActivity(intent)
        }
        binding.tiktokContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(tiktok)
            startActivity(intent)
        }
        binding.githubContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(github)
            startActivity(intent)
        }

        binding.contactBack.setOnClickListener {
            finish()
        }
    }
}