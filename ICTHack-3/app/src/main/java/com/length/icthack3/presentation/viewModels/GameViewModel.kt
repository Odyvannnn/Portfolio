package com.length.icthack3.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.length.icthack3.data.UserRepositoryImpl
import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.usecase.GetUserListAsyncUseCase
import com.length.icthack3.domain.usecase.GetUserListUseCase
import com.length.icthack3.domain.usecase.GetUserUseCase
import com.length.icthack3.presentation.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val repository = UserRepositoryImpl(Firebase.firestore)
    private val prefs = Application.prefs

    private val getUserListUseCase = GetUserListUseCase(repository)
    private val getUserUseCase = GetUserUseCase(repository)
    private val getUserListAsyncUseCase = GetUserListAsyncUseCase(repository)

    var usersData: LiveData<List<User>> = getUserListUseCase.getUserList()
    val user = MutableLiveData<User>(prefs.getUser())

    fun getAnimals(){

    }

    fun getUserProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            val userDat = getUserUseCase.getUser(user.value!!.token)
            user.value = userDat!!
        }
    }


    fun getUserList(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserListAsyncUseCase.getUserListAsync()
        }
    }
}