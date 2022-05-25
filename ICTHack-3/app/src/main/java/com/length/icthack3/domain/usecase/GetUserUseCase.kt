package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {

    suspend fun getUser(userId: String): User? {
        return repository.getUser(userId)
    }
}