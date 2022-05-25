package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.repository.UserRepository

class GetUserListAsyncUseCase(private val repository: UserRepository) {

    suspend fun getUserListAsync() {
        repository.getUserListAsync()
    }
}