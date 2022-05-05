package com.psg.gunstagram.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ActivityMainBinding
import com.psg.gunstagram.view.base.BaseActivity
import com.psg.gunstagram.view.login.LoginActivity
import com.psg.gunstagram.view.login.LoginViewModel
import com.psg.gunstagram.view.navi.AlarmFragment
import com.psg.gunstagram.view.navi.DetailViewFragment
import com.psg.gunstagram.view.navi.GridFragment
import com.psg.gunstagram.view.navi.UserFragment
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(R.layout.activity_main), BottomNavigationView.OnNavigationItemSelectedListener {
    override val TAG: String = MainActivity::class.java.simpleName
    override val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initView()
    }

    override fun initView() {
        binding.bnvMain.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                val detailViewFragment = DetailViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, detailViewFragment).commit()
                return true
            }
            R.id.action_search -> {
                val gridFragment = GridFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, gridFragment).commit()
                return true
            }
            R.id.action_add_photo -> {
                return true
            }
            R.id.action_favorite -> {
                val alarmFragment = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, alarmFragment).commit()
                return true
            }
            R.id.action_account -> {
                val userFragment = UserFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_main, userFragment).commit()
                return true
            }
        }
        return false
    }
}