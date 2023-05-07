package com.example.phakezalo.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phakezalo.R
import com.example.phakezalo.databinding.FragmentInfoBinding
import com.example.phakezalo.ui.activities.ContactActivity
import com.example.phakezalo.ui.activities.MyCloudActivity

class InfoFragment : Fragment(R.layout.fragment_info) {
    private lateinit var binding : FragmentInfoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerInfo.setOnClickListener {
            startActivity(Intent(requireActivity(), ContactActivity::class.java))
        }

        binding.myCloudContainer.setOnClickListener {
            startActivity(Intent(requireActivity(), MyCloudActivity::class.java))
        }
    }
}