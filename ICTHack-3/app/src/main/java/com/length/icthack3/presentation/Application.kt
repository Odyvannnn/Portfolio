package com.length.icthack3.presentation

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.length.icthack3.data.prefernces.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Application : MultiDexApplication() {


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(this)
        CoroutineScope(Dispatchers.IO).launch {
//            TODO("Singleton scoping")
        }
    }

    companion object {
        lateinit var prefs: Prefs
            private set
    }
}