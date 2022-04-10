package com.example.dacs3.ui.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dacs3.R
import com.example.dacs3.Repository.MainRepository
import com.example.dacs3.Util.const.HOST_LINK
import com.example.dacs3.data.retrofit.ApiRetrofit
import com.example.dacs3.databinding.FragmentOptionsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class OptionsFragment : Fragment() {

    @Inject
    lateinit var apiRetrofit: ApiRetrofit

    @Inject
    lateinit var mainRepository: MainRepository

    lateinit var binding: FragmentOptionsBinding

    lateinit var gson: Gson
    lateinit var proviceRetrofit: Retrofit.Builder
    lateinit var proviceMovieService: ApiRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)
        binding = FragmentOptionsBinding.bind(view)

        gson = GsonBuilder().create()

        proviceRetrofit = Retrofit.Builder()
            .baseUrl(HOST_LINK)
            .addConverterFactory(GsonConverterFactory.create(gson))

        proviceMovieService = proviceRetrofit.build().create(ApiRetrofit::class.java)

        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            try {
//                val res = mainRepository.getListMovie(0)
//
//                res.forEach {
//                    binding.text.append("\n ---${it.title}---")
//                    it.listFilm.forEach {
//                        binding.text.append("\n+ ${it.name} ${it.id} ${it.category}")
//                    }
//                }
//
//                val res2 = mainRepository.getDataDetailMovie(12228, 1)
//                    binding.text.append(res2.toString())

//                val res4 = mainRepository.getListMovie2( 1)
//                    binding.text.append(res4.toString())


//                val res3 = mainRepository.getMediaMovie(
//                    category = 1,
//                    contentId = 12135,
//                    episodeId = 53466,
//                    definition = "GROOT_HD"
//                )
//
//                    binding.text.append(res3.toString())

                val res4 = mainRepository.searchWithKeyWord("tenki no ko")

                binding.text.append((res4.toString()))

                val database = Firebase.database
                val myRef = database.getReference("msg")


                // Read from the database
                myRef.addValueEventListener(object: ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        val value = snapshot.getValue<String>()
                        Log.e("CheckLog", "Value is: " + value)
                        binding.text.text = value
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("CheckLog", "Failed to read value.", error.toException())
                    }

                })


            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        }


        // Inflate the layout for this fragment
        return view
    }


}