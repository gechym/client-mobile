package com.example.dacs3.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.dacs3.R
import com.example.dacs3.databinding.ActivityMainBinding
import com.example.dacs3.databinding.MangHinhLoadingBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var bindingLoadingBinding: MangHinhLoadingBinding
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingLoadingBinding = MangHinhLoadingBinding.inflate(layoutInflater)


        setContentView(bindingLoadingBinding.root) //TODO Mang Hinh Loading


        //TODO Chuyen mang hinh chính
        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            delay(2000L)

            setContentView(binding.root)
            navController = findNavController(R.id.fragmentContainerView)
            binding.bottomBarNav.setupWithNavController(navController)


        }


        //Hide system bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }




}