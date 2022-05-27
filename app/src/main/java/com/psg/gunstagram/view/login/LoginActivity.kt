package com.psg.gunstagram.view.login

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseUser
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ActivityLoginBinding
import com.psg.gunstagram.util.AppLogger
import com.psg.gunstagram.util.Event
import com.psg.gunstagram.view.base.BaseActivity
import com.psg.gunstagram.view.base.BaseViewModel
import com.psg.gunstagram.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(R.layout.activity_login) {
    override val TAG: String = LoginActivity::class.java.simpleName
    override val viewModel: LoginViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initView()
//        setEventFlow()
    }


    override fun handleEvent(event: Event) = when (event){
        is Event.ShowToast ->
            CoroutineScope(Dispatchers.Main).launch {
                makeToast(event.text)
            }
        is Event.SignEmail ->
            CoroutineScope(Dispatchers.Main).launch {
                signEmail(event.user)
            }
        is Event.SignGoogle ->
            CoroutineScope(Dispatchers.Main).launch {
                signGoogle(event.intent,event.code)
            }

        else -> {}
    }

    override fun initView(){
        binding.activity = this
        binding.vm = viewModel

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == viewModel.GOOGLE_LOGIN_CODE){
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result!!.isSuccess){
                val account = result.signInAccount
                viewModel.firebaseAuthWithGoogle(account)
            }
        }
    }

    private fun signEmail(user: FirebaseUser?) {
        if (user != null) startActivity(Intent(this, MainActivity::class.java))
    }

    private fun signGoogle(intent: Intent, loginCode: Int) {
        startActivityForResult(intent,loginCode)
    }


}