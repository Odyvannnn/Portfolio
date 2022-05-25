package com.length.icthack3.presentation.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.startActivityAndFinish(packageContext: Context, cls: Class<*>) {
    val i = Intent(packageContext, cls)
    startActivity(i)
    finish()
}