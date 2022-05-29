package com.psg.gunstagram.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ActivityMainBinding
import com.psg.gunstagram.util.Constants
import com.psg.gunstagram.view.base.BaseActivity
import com.psg.gunstagram.view.navi.AlarmFragment
import com.psg.gunstagram.view.navi.GridFragment
import com.psg.gunstagram.view.navi.detail.DetailFragment
import com.psg.gunstagram.view.navi.user.UserFragment
import com.psg.gunstagram.view.photo.AddPhotoActivity
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main),
    BottomNavigationView.OnNavigationItemSelectedListener {
    override val TAG: String = MainActivity::class.java.simpleName
    override val viewModel: MainViewModel by inject()
    private lateinit var detailViewFragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initView() {
        detailViewFragment = DetailFragment()
        initFragment("home")
        binding.bnvMain.setOnNavigationItemSelectedListener(this)

        binding.bnvMain.selectedItemId = R.id.action_home

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                initFragment(Constants.FRAGMENT_HOME)
                return true
            }
            R.id.action_search -> {
                initFragment(Constants.FRAGMENT_SEARCH)
                return true
            }
            R.id.action_add_photo -> {
                checkPermission()
                return true
            }
            R.id.action_favorite -> {
                initFragment(Constants.FRAGMENT_FAVORITE)
                return true
            }
            R.id.action_account -> {
                initFragment(Constants.FRAGMENT_ACCOUNT)
                return true
            }
        }
        return false
    }

    private fun initFragment(key: String) {
        when (key) {
           Constants.FRAGMENT_HOME -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, detailViewFragment)
                    .commit()
            }
            Constants.FRAGMENT_SEARCH -> {
                val gridFragment = GridFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, gridFragment)
                    .commit()
            }
            Constants.FRAGMENT_FAVORITE -> {
                val alarmFragment = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, alarmFragment)
                    .commit()
            }
            Constants.FRAGMENT_ACCOUNT -> {
                val userFragment = UserFragment()
                val bundle = Bundle()
                bundle.putString("destinationUid",FirebaseAuth.getInstance().currentUser?.uid)
                userFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, userFragment)
                    .commit()
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(this, AddPhotoActivity::class.java))
        } else {
            requestPermission()
        }
    }
}