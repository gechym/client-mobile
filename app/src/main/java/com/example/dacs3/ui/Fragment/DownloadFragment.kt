package com.example.dacs3.ui.Fragment

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentDownloadBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DownloadFragment : Fragment() {

    lateinit var binding: FragmentDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_download, container, false)

        binding = FragmentDownloadBinding.bind(view)






        binding.btnPushData.setOnClickListener {
            Toast.makeText(requireContext(), "this", Toast.LENGTH_SHORT).show()




            val database = Firebase.database
            val myRef = database.getReference("msg")

            Log.e("CheckLog",myRef.toString())

            myRef.setValue(binding.editTextTextPersonName.text.toString())
        }



        return view
    }



}