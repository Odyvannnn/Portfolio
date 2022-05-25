package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.UserRepository

class DeleteUserUseCase(private val repository: UserRepository) {

    suspend fun deleteUser(user: User) {
        repository.deleteUser(user)
    }
}