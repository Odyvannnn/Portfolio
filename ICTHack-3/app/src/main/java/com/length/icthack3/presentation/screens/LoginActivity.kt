package com.length.icthack3.presentation.screens

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.length.icthack3.R
import com.length.icthack3.presentation.util.startActivityAndFinish
import com.length.icthack3.presentation.viewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val userToken = findViewById<TextInputEditText>(R.id.editLogin)
        val navigateToMainButton = findViewById<MaterialButton>(R.id.toMainButton)
        val navigateToRegistrationButton = findViewById<TextView>(R.id.toRegistrationFragment)

        navigateToMainButton.setOnClickListener {
            viewModel.checkUserExistToLogin(userToken.text.toString())
        }

        viewModel.userAuthResult.observe(this) {
            if (it){
                startActivityAndFinish(this, GameActivity::class.java)
            }else{
                toastOnWrongData()
            }
        }

        navigateToRegistrationButton.setOnClickListener {
            viewModel.addNewUser()
            startActivityAndFinish(this, GameActivity::class.java)
        }
    }


    private fun toastOnWrongData(){
        val text = "Проверьте токен"
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(this, text, duration).show()
    }

}