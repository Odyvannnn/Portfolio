package com.length.icthack3.domain.usecase

import androidx.lifecycle.LiveData
import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.UserRepository

class GetUserListUseCase(private val repository: UserRepository) {

    fun getUserList(): LiveData<List<User>> {
        return repository.getUserList()
    }
}