package com.length.icthack3.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.length.icthack3.presentation.screens.GameActivity
import com.length.icthack3.presentation.screens.LoginActivity
import com.length.icthack3.presentation.util.startActivityAndFinish


class SplashActivity : AppCompatActivity() {

    private val prefs = Application.prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateFromSplash()
    }

    private fun navigateFromSplash() {
        if (prefs.getUser() == null) {
            startActivityAndFinish(this, LoginActivity::class.java)
        } else {
            startActivityAndFinish(this, GameActivity::class.java)
        }
    }


}