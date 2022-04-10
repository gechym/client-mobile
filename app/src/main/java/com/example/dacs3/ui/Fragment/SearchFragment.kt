package com.example.dacs3.ui.Fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.bind(inflater.inflate(R.layout.fragment_search, container, false))
//        val testList : MutableList<test> = mutableListOf()
//        testList.add(test("bao","1"))
//        testList.add(test("bao","1"))
//        testList.add(test("bao","1"))
//        testList.add(test("bao","1"))
//        testList.add(test("bao","1"))


        val testList : Array<String>  = arrayOf("bao","bao1","bao2","bao3","bao4","bao5")

        val arrayAdapter = ArrayAdapter<String>(requireContext(),R.layout.drowdown_item,testList)



        return inflater.inflate(R.layout.fragment_search, container, false)
    }



}


