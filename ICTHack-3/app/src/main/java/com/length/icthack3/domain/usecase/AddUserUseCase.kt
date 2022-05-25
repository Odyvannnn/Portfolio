package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.UserRepository

class AddUserUseCase(private val repository: UserRepository) {

    suspend fun addUser(user: User) {
        repository.addUser(user)
    }
}