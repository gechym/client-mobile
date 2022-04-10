package com.example.dacs3.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentInfoUserBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class InfoUserFragment : Fragment() {

    lateinit var binding: FragmentInfoUserBinding
    lateinit var mAuth: FirebaseAuth


    lateinit var googleSignInClient: GoogleSignInClient
    private var web_Client_ID = "397884200789-9sv1nsqbrf0ik3c8kbcreau1en4c24mf.apps.googleusercontent.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_user, container, false)
        binding = FragmentInfoUserBinding.bind(view)

        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(web_Client_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(),gson)


        mAuth = FirebaseAuth.getInstance()

        initView()

        return view
    }


    private fun initView() {
        val user = mAuth.currentUser

        if (user != null) {
            binding.email.append(user.email)


            Glide.with(requireContext())
                .load(user.photoUrl)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.animation_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(binding.avtUser);

            binding.logout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                activity?.finish()

            }

        }


    }


}