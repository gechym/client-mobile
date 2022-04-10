package com.example.dacs3.ui.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dacs3.R
import com.example.dacs3.Repository.MainRepository
import com.example.dacs3.databinding.ActivitySearchFragmentBinding
import com.example.dacs3.ui.adapter.FlilmAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragmentActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchFragmentBinding

    @Inject
    lateinit var mainRepository: MainRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFragmentBinding.inflate(layoutInflater)


        val typeFilm: MutableList<test> = mutableListOf()
        typeFilm.add(test("TV Series", "TV,SETI,MINISERIES,VARIETY,TALK,COMIC,DOCUMENTARY"))
        typeFilm.add(test("Movie", "MOVIE,TVSPECIAL"))
        typeFilm.add(test("Anime", "COMIC"))


        val category: MutableList<test> = mutableListOf()
        category.add(test("All Categories", ""))
        category.add(test("Drama", "8"))
        category.add(test("Suspense", "16"))
        category.add(test("Sci-Fi", "19"))
        category.add(test("Action", "1"))
        category.add(test("Romance", "18"))
        category.add(test("Fantasy", "10"))
        category.add(test("Horror", "13"))
        category.add(test("Comedy", "5"))
        category.add(test("Crime", "6"))
        category.add(test("Adventure", "2"))
        category.add(test("Animation", "3"))
        category.add(test("Thriller", "23"))
        category.add(test("Family", "9"))
        category.add(test("Musical", "63,14,15"))
        category.add(test("War", "24"))
        category.add(test("LGBTQ", "65"))
        category.add(test("Catastrophe", "64"))

        val area: MutableList<test> = mutableListOf()
        area.add((test("All regions", "")))
        area.add((test("America", "61")))
        area.add((test("Korea", "53")))
        area.add((test("U.K", "60")))
        area.add((test("Japan", "44")))
        area.add((test("Thailand", "57")))
        area.add((test("Europe", "37,60,58,50,54,55,48,46,45,34,35,38,39,43,62")))
        area.add((test("China", "32,56")))
        area.add((test("India", "40")))
        area.add((test("Australia", "27")))
        area.add((test("Indonesia", "41")))
        area.add((test("other", "26,28,29,30,31,33,36,42,47,49,59")))

        val year: MutableList<test> = mutableListOf()
        year.add(test("All Time Periods", ""))
        year.add(test("2022,2022", "2022,2022"))
        year.add(test("2021,2021", "2021,2021"))
        year.add(test("2020,2020", "2020,2020"))
        year.add(test("2019,2019", "2018,2018"))
        year.add(test("2017,2017", "2017,2017"))
        year.add(test("2016,2016", "2016,2016"))
        year.add(test("2011,2015", "2011,2015"))
        year.add(test("2010-2000", "2010-2000"))
        year.add(test("1900,2009", "1900,2009"))


        val status: MutableList<test> = mutableListOf()
        status.add(test("Recent", "up"))
        status.add(test("Popularity", "count"))


        val arrayAdapter = ArrayAdapter(this, R.layout.drowdown_item, typeFilm)
        binding.typeFilm.setAdapter(arrayAdapter)

        val arrayAdapterCategory = ArrayAdapter(this, R.layout.drowdown_item, category)
        binding.category.setAdapter(arrayAdapterCategory)

        val arrayAdapterArea = ArrayAdapter(this, R.layout.drowdown_item, area)
        binding.area.setAdapter(arrayAdapterArea)

        val arrayAdapterYear = ArrayAdapter(this, R.layout.drowdown_item, year)
        binding.year.setAdapter(arrayAdapterYear)

        val arrayAdapterStatus = ArrayAdapter(this, R.layout.drowdown_item, status)
        binding.status.setAdapter(arrayAdapterStatus)



        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            val listSearch = mainRepository.getSearchMovie(
                SearchPost(
                    params = getParams(binding.typeFilm.text.split("-")),
                    category = getParams(binding.category.text.split("-")),
                    area = getParams(binding.area.text.split("-")),
                    year = getParams(binding.year.text.split("-")),
                    order = getParams(binding.status.text.split("-"))
                )
            )

            binding.recyclerviewSearchList.setHasFixedSize(true)
            binding.recyclerviewSearchList.layoutManager = GridLayoutManager(applicationContext, 3)
            binding.recyclerviewSearchList.adapter = FlilmAdapter(listSearch,false,this@SearchFragmentActivity)
        }



        setContentView(binding.root)
        //Hide system bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        initEvent()
    }

     fun initEvent() {
        binding.button2.setOnClickListener {

            val type = binding.typeFilm.text.split("-")
            val category = binding.category.text.split("-")
            val area = binding.area.text.split("-")
            val year = binding.year.text.split("-")
            val status = binding.status.text.split("-")

            val scopeMain = CoroutineScope(Dispatchers.Main)
            scopeMain.launch {
                val listSearch = mainRepository.getSearchMovie(
                    SearchPost(
                        params = getParams(type),
                        category = getParams(category),
                        area = getParams(area),
                        year = getParams(year),
                        order = getParams(status)
                    )
                )

                if (listSearch.size == 0) {
                    Toast.makeText(this@SearchFragmentActivity, "không có phim nào phù hợp với kết quả tìm kiếm", Toast.LENGTH_SHORT).show()
                }else{
                    binding.recyclerviewSearchList.setHasFixedSize(true)
                    binding.recyclerviewSearchList.layoutManager = GridLayoutManager(applicationContext, 3)
                    binding.recyclerviewSearchList.adapter = FlilmAdapter(listSearch,false,this@SearchFragmentActivity)
                }

            }
        }


         binding.button3.setOnClickListener {
             val scopeMain = CoroutineScope(Dispatchers.Main)
             scopeMain.launch {
                 val listSearchKWResults = mainRepository.searchWithKeyWord(binding.search.text.toString())

                 if (listSearchKWResults.size == 0) {
                     Toast.makeText(this@SearchFragmentActivity, "không có phim nào phù hợp với kết quả tìm kiếm", Toast.LENGTH_SHORT).show()
                 }else{
                     binding.recyclerviewSearchList.setHasFixedSize(true)
                     binding.recyclerviewSearchList.layoutManager = GridLayoutManager(applicationContext, 3)
                     binding.recyclerviewSearchList.adapter = FlilmAdapter(listSearchKWResults,false,this@SearchFragmentActivity)
                 }

             }
         }


         binding.back.setOnClickListener {
             finish()
         }
    }

    fun getParams(list: List<String>): String {
        if (list.size == 1) {
            return ""
        } else {
            return list[1]
        }
    }

}

data class SearchPost(
    @SerializedName("params") @Expose
    val params: String,

    @SerializedName("area") @Expose
    val area: String,

    @SerializedName("category") @Expose
    val category: String,

    @SerializedName("year") @Expose
    val year: String,

    @SerializedName("order") @Expose
    val order: String
)


data class test(private val name: String, private val params: String) {
    override fun toString(): String {
        return "${name}-${params}"
    }
}