package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.UserRepository

class EditUserUseCase(private val repository: UserRepository) {

    suspend fun editUser(user: User) {
        repository.editUser(user)
    }
}