package com.example.phakezalo.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.phakezalo.R
import com.example.phakezalo.databinding.ActivityMainBinding
import com.example.phakezalo.ui.fragments.DiscoverFragment
import com.example.phakezalo.ui.fragments.FriendFragment
import com.example.phakezalo.ui.fragments.NoteFragment
import com.example.phakezalo.ui.fragments.InfoFragment
import com.example.phakezalo.ui.fragments.PhonebookFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            replaceFragment(FriendFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.act_chat -> {
                    replaceFragment(FriendFragment())
                    true
                }
                R.id.act_phonebook -> {
                    replaceFragment(PhonebookFragment())
                    true
                }
                R.id.act_disc -> {
                    replaceFragment(DiscoverFragment())
                    true
                }
                R.id.act_note -> {
                    replaceFragment(NoteFragment())
                    true
                }
                R.id.act_prof -> {
                    replaceFragment(InfoFragment())
                    true
                }
                else -> {
                    replaceFragment(FriendFragment())
                    true
                }
            }
        }
    }

    private fun replaceFragment(frag:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerFrag, frag)
        fragmentTransaction.commit()
    }

}