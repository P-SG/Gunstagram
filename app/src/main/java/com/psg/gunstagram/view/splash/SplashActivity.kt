package com.psg.gunstagram.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.psg.gunstagram.R
import com.psg.gunstagram.view.login.LoginActivity
import com.psg.gunstagram.view.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkAutoLogin()

    }

    private fun checkAutoLogin(){

        startApp(auth.currentUser != null)

    }

    private fun startApp(autoLogin: Boolean) {
        if(autoLogin){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },3000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            },3000)
        }


    }
}