package com.psg.gunstagram.view.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.auth.FirebaseUser
import com.psg.gunstagram.util.AppLogger
import com.psg.gunstagram.view.main.MainActivity

abstract class BaseActivity<T: ViewDataBinding, V: BaseViewModel>(@LayoutRes val res: Int): AppCompatActivity() {
    lateinit var binding: T
    abstract val TAG: String
    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, res)
        binding.lifecycleOwner = this
    }

    open fun makeToast(msg:String) = Toast.makeText(this ,msg, Toast.LENGTH_SHORT).show()

    open fun signEmail(user: FirebaseUser?) {
        if (user != null) startActivity(Intent(this,MainActivity::class.java))
    }

    open fun signGoogle(intent: Intent, loginCode: Int) {
        startActivityForResult(intent,loginCode)
    }

    open fun initView(){

    }

    override fun onStart() {
        super.onStart()
        AppLogger.i(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        AppLogger.i(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        AppLogger.i(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        AppLogger.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLogger.i(TAG,"onDestroy")
    }

}