package com.length.icthack3.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.length.icthack3.data.UserRepositoryImpl
import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.usecase.AddUserUseCase
import com.length.icthack3.domain.usecase.GetUserUseCase
import com.length.icthack3.presentation.util.randomId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val repository = UserRepositoryImpl(Firebase.firestore)

    private val getUserUseCase = GetUserUseCase(repository) // Возвращает null, если не нашел
    private val addUserUseCase = AddUserUseCase(repository)

    var userAuthResult = MutableLiveData<Boolean>()

    fun checkUserExistToLogin(userId: String){
        viewModelScope.launch {
            val user = getUserUseCase.getUser(userId)
            userAuthResult.value = user != null
        }
    }

    fun addNewUser() {
        viewModelScope.launch (Dispatchers.IO) {
            val newUser = User(
                username = "user",
                token = randomId(),
                exchangeToken = randomId()
            )
            addUserUseCase.addUser(newUser)
        }
    }
}