package com.example.dacs3.ui.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentMyListBinding
import com.example.dacs3.module.Film
import com.example.dacs3.module.MyListMovie
import com.example.dacs3.ui.adapter.FlilmAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MyListFragment : Fragment() {

    lateinit var binding: FragmentMyListBinding
    lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_my_list, container, false)

        binding = FragmentMyListBinding.bind(view)

        mAuth = FirebaseAuth.getInstance()


        binding.text.text = mAuth.currentUser?.email ?: ""
        var currentUser = mAuth.currentUser
        if (currentUser != null) {
            try {
                val nameRef = currentUser.email?.split("@")?.get(0)

                val database = Firebase.database
                val myRef = database.getReference("${nameRef.toString()}/myList")

                binding.text.text = nameRef



                myRef.addValueEventListener(object  : ValueEventListener{
                    var listFilmAdapter: MutableList<Film> = mutableListOf()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listFilmAdapter = mutableListOf()
                        for (userSnapshot in snapshot.children) {
                            val myListMovie = userSnapshot.getValue(MyListMovie::class.java)
                            if (myListMovie != null){
                                listFilmAdapter.add(
                                    Film(
                                        id= myListMovie.id!!,
                                        image = 0,
                                        imageUrl = myListMovie.image!!,
                                        banner = "",
                                        category = myListMovie.category!!,
                                        description = "",
                                        epvisodeTotal = 0,
                                        name = "",
                                        yearRelease = 2
                                    )
                                )
                            }

                        }

                        Log.e("CheckLog",listFilmAdapter.toString())

                        binding.recyclerviewMylist.setHasFixedSize(true)
                        binding.recyclerviewMylist.layoutManager = GridLayoutManager(requireContext(), 3)
                        binding.recyclerviewMylist.adapter = FlilmAdapter(listFilmAdapter,false,requireContext())
                    }






                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                    
                })

            }catch (e : Exception){
                val nameRef = currentUser.email?.split(".")?.get(0)

                val database = Firebase.database
                val myRef = database.getReference("${nameRef.toString()}/myList")

                binding.text.text = nameRef

                myRef.addValueEventListener(object  : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val myListMovie = userSnapshot.getValue(MyListMovie::class.java)
                            Log.e("CheckLog", myListMovie.toString())

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

        }

        return view
    }




}