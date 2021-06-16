package com.nezamipour.mehdi.userinfo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nezamipour.mehdi.userinfo.NavHostActivity
import com.nezamipour.mehdi.userinfo.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private lateinit var ui: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(ui.root)

        lifecycleScope.launchWhenCreated {
            delay(2000)
            startActivity(Intent(this@SplashActivity, NavHostActivity::class.java))
            finish()
        }
    }
}