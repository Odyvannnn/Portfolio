package com.length.icthack3.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.length.icthack3.data.UserRepositoryImpl
import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.usecase.GetUserListAsyncUseCase
import com.length.icthack3.domain.usecase.GetUserListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RateViewModel: ViewModel() {

    private val repository = UserRepositoryImpl(Firebase.firestore)

    private val getUserListUseCase = GetUserListUseCase(repository)
    private val getUserListAsyncUseCase = GetUserListAsyncUseCase(repository)

    var usersData: LiveData<List<User>> = getUserListUseCase.getUserList()

    fun getUserList(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserListAsyncUseCase.getUserListAsync()
        }
    }


}