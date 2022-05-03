package com.example.dacs3.ui.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentLoginBinding
import com.example.dacs3.module.MyListMovie
import com.example.dacs3.module.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    lateinit var mAuth: FirebaseAuth

    lateinit var googleSignInClient: GoogleSignInClient
    private var web_Client_ID =
        "397884200789-9sv1nsqbrf0ik3c8kbcreau1en4c24mf.apps.googleusercontent.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(web_Client_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gson)

        mAuth = FirebaseAuth.getInstance()

        binding = FragmentLoginBinding.bind(view)

        initEvent()


        return view
    }

    fun initEvent() {
        binding.signInButton.setOnClickListener {
            signIn()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginAccount(email, password)
            } else {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    fun loginAccount(email: String, password: String) {

        val mAuth = mAuth

        mAuth.signOut()

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {

//                it.result.user.

                Toast.makeText(requireContext(), "Login successfully", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val controller = findNavController()


        binding.register.setOnClickListener {
            try {
                controller.navigate(R.id.registerFragment)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT).show()
                Log.e("CheckLog", e.toString())
            }
        }
    }

    private fun signIn() {
        val sifgnInIntent = googleSignInClient.signInIntent
        startActivityForResult(sifgnInIntent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.e("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.e("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.e("SignInActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext(), "Đăng nhập thành công ", Toast.LENGTH_SHORT)
                        .show()


//                    val currentUser = task.result.user
//                    if (currentUser != null) {
//                        try {
//                            val nameRef = currentUser.email?.split("@")?.get(0)
//
//                            val database = Firebase.database
//                            val myRef = database.getReference(nameRef.toString())
//
//
//                        }catch (e : Exception){
//                            val nameRef = currentUser.email?.split(".")?.get(0)
//
//                            val database = Firebase.database
//                            val myRef = database.getReference(nameRef.toString())
//
//
//                        }
//
//                    }

                    activity?.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT)
                        .show()

                }
            }
    }

    override fun onResume() {
        super.onResume()

        if (mAuth.currentUser != null) {
            activity?.finish()
        }

    }


}