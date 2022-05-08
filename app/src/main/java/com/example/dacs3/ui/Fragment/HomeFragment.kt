package com.example.dacs3.ui.Fragment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dacs3.R
import com.example.dacs3.Repository.MainRepository
import com.example.dacs3.databinding.FragmentHomeBinding
import com.example.dacs3.databinding.LayoutErrorDialogBinding
import com.example.dacs3.module.CategoryFilm
import com.example.dacs3.ui.activity.UserActivity
import com.example.dacs3.ui.adapter.CategoryAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: CategoryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var mAuth: FirebaseAuth

    companion object{
        var page = 1;
    }


    @Inject
    lateinit var mainRepository: MainRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)

        mAuth = FirebaseAuth.getInstance()

        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            initEvent()
//            renderDataView()
        }

        return view
    }

    suspend fun initEvent(){
        swipeRefreshLayout = binding.swipe
        swipeRefreshLayout.setOnRefreshListener(this)

        binding.avtUser.setOnClickListener {
            val intent = Intent(requireContext(),UserActivity::class.java)
            startActivity(intent)
        }


        binding.search.setOnClickListener {
            val intent = Intent(requireContext(),SearchFragmentActivity::class.java)
            startActivity(intent)
        }

        if (mAuth.currentUser != null){
            Glide.with(requireContext())
                .load(mAuth.currentUser!!.photoUrl)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.animation_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(binding.avtUser);
        }



        renderDataView()

    }

    suspend fun fetchData(page: Int): MutableList<CategoryFilm> {
        return mainRepository.getListMovie(page).toMutableList()
    }

    suspend fun renderDataView() {
        try {

            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
            adapter = CategoryAdapter(fetchData(1), requireContext())
            binding.recyclerview.adapter = adapter


            adapter.addListMore(fetchData(2))
            adapter.addListMore(fetchData(3))
            adapter.addListMore(fetchData(4))

            adapter.addListMore(fetchData(5))
            adapter.addListMore(fetchData(6))
            adapter.addListMore(fetchData(7))

            adapter.addListMore(fetchData(8))
            adapter.addListMore(fetchData(9))
            adapter.addListMore(fetchData(10))

//            // render data slider
//            val imageList = ArrayList<SlideModel>() // Create image list
//            val dataSlider = fetchData(0)[0].listFilm
//
//            dataSlider.forEach {
//                imageList.add(SlideModel(it.imageUrl, it.name,ScaleTypes.CENTER_CROP))
//            }
//            binding.imageSlider.setImageList(imageList)
//
//            binding.imageSlider.setItemClickListener(object : ItemClickListener {
//                override fun onItemSelected(position: Int) {
//                    val film = dataSlider[position]
//
//                    bottomSheetDialog = BottomSheetDialog(requireContext())
//
//                    val bottomSheetDialogLayout = LayoutInflater.from(requireContext())
//                        .inflate(R.layout.bottom_sheet_dialog, null, false)
//
//                    val bindingBottomSheetDialog = BottomSheetDialogBinding.bind(bottomSheetDialogLayout)
//
//                    bindingBottomSheetDialog.apply {
//                        this.btnPlay.setOnClickListener {
//
//
//                            bottomSheetDialog.dismiss()
//
//
//
//                            val intent = Intent(requireContext(), DetailActivity::class.java)
//                            intent.putExtra("id", film.id)
//                            intent.putExtra("category", film.category)
//
//                            ContextCompat.startActivity(requireContext(),intent,null)
//                        }
//
//                        Glide.with(requireContext())
//                            .load(film.imageUrl)
//                            .placeholder(R.drawable.animation_loading)
//                            .error(R.drawable.img_2)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .priority(Priority.HIGH)
//                            .into(this.image);
//
//                        this.title.text = film.name
//                        this.description.text = film.description
//                    }
//
//
//
//                    bottomSheetDialog.setContentView(bottomSheetDialogLayout)
//                    bottomSheetDialog.show()
//                }
//            })




        } catch (e: Exception) {
            val successDialog = LayoutInflater.from(requireContext())
                .inflate(R.layout.layout_error_dialog, binding.root, false)
            val bindingSuccessDialog = LayoutErrorDialogBinding.bind(successDialog)

            bindingSuccessDialog.textMsg.text =
                "$e :\r\r \n Dữ liệu đang được trả về vui lòng đợi"


            val dialog = MaterialAlertDialogBuilder(requireContext()).setView(successDialog)
                .setBackground(
                    ColorDrawable(0x00000000.toInt())
                ).create()

            bindingSuccessDialog.btnAction.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()

        if (mAuth.currentUser != null){
            Glide.with(requireContext())
                .load(mAuth.currentUser!!.photoUrl)
                .placeholder(R.drawable.avt)
                .error(R.drawable.avt)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(binding.avtUser);

            val scopeMain = CoroutineScope(Dispatchers.Main)
            scopeMain.launch {
                renderDataView()
                swipeRefreshLayout.isRefreshing = false
            }

        }else{
            binding.avtUser.setImageResource(R.drawable.ic_baseline_login_24)

            val scopeMain = CoroutineScope(Dispatchers.Main)
            scopeMain.launch {
                renderDataView()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onRefresh() {
        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            adapter.clearData()
            adapter.addListMore(fetchData(3))
            adapter.addListMore(fetchData(Random.nextInt(4,7)))
            adapter.addListMore(fetchData(Random.nextInt(7,11)))
            swipeRefreshLayout.isRefreshing = false
        }

    }
}