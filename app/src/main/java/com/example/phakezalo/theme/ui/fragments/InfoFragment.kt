package com.example.phakezalo.theme.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.phakezalo.R
import com.example.phakezalo.theme.ui.activities.ContactActivity

class InfoFragment : Fragment(R.layout.fragment_personal) {
    private lateinit var contact: ConstraintLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contact = view.findViewById(R.id.containerInfo)
        contact.setOnClickListener {
            startActivity(Intent(requireActivity(), ContactActivity::class.java))
        }
    }
}