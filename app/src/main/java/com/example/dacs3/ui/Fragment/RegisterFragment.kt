package com.example.dacs3.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentRegisterBinding
import com.example.dacs3.module.MyListMovie
import com.example.dacs3.module.User
import com.example.dacs3.ui.activity.UserActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        binding = FragmentRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val passwordConfig = binding.passwordConfigEditText.text.toString()


            if(email.isNotEmpty() && password.isNotEmpty() && passwordConfig.isNotEmpty()){
                if (password.equals(passwordConfig)){

                    registerAccount(email,password)


                }else{
                    Toast.makeText(requireContext(),"Confirmation password is not the same",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
            }


        }


        return view
    }


    fun registerAccount(email: String, password: String){

        val mAuth = UserActivity.mAuth

        mAuth.signOut()

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                val currentUser = it.result.user
                if (currentUser != null) {
                    try {
                        val nameRef = currentUser.email?.split("@")?.get(0)

                        val database = Firebase.database
                        val myRef = database.getReference(nameRef.toString())

                        myRef.setValue(
                            User(
                                email = currentUser.email,
                                isMenbership = false,
                                myListMovie = MyListMovie(),
                                avata = "https://upload.wikimedia.org/wikipedia/commons/0/0b/Netflix-avatar.png"
                            )
                        )
                    }catch (e : Exception){
                        val nameRef = currentUser.email?.split(".")?.get(0)

                        val database = Firebase.database
                        val myRef = database.getReference(nameRef.toString())

                        myRef.setValue(
                            User(
                                email = currentUser.email,
                                isMenbership = false,
                                myListMovie = MyListMovie(),
                                avata = "https://upload.wikimedia.org/wikipedia/commons/0/0b/Netflix-avatar.png"
                            )
                        )
                    }

                }
                Toast.makeText(requireContext(), "Register account successfully", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


}