package com.example.dacs3.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.example.dacs3.ui.Fragment.InfoUserFragment
import com.example.dacs3.ui.Fragment.LoginFragment
import com.example.dacs3.R
import com.example.dacs3.databinding.ActivityUserBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class UserActivity : AppCompatActivity() {

     lateinit var binding: ActivityUserBinding
     companion object{
         lateinit var mAuth: FirebaseAuth
     }

    lateinit var googleSignInClient: GoogleSignInClient
    private var web_Client_ID = "397884200789-9sv1nsqbrf0ik3c8kbcreau1en4c24mf.apps.googleusercontent.com"


    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.frameLayout) as NavHostFragment
        navController = navHostFragment.navController

        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(web_Client_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gson)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser






        //kiểm tra đăng nhập
        if (user != null) {
            Toast.makeText(this, "Bạn đã đăng nhập", Toast.LENGTH_SHORT).show()
//            setFragment(InfoUserFragment())
//
//            fin

            navController.navigate(R.id.infoUserFragment)




        }else{
//            setFragment(LoginFragment())

            navController.navigate(R.id.loginFragment)

        }

        binding.back.setOnClickListener {
            finish()
        }

    }


    override fun onSupportNavigateUp(): Boolean { // cấu hình
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    private fun setFragment(fragment: Fragment) { // chuyển đổi fragment
//        val transaction = supportFragmentManager.beginTransaction() // biến giúp việt chuyển fragment
//        transaction.replace(R.id.frameLayout, fragment) // chuyển đổi fragment
//        transaction.disallowAddToBackStack() // khi chuyên fragment thì fragment ban đầu sẽ đc gỡ ra bộ nhớ giúp tiết kiệm
//        transaction.commit()
//    }
}