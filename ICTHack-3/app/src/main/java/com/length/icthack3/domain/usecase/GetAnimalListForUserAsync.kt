package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.AnimalRepository

class GetAnimalListForUserAsync(private val repository: AnimalRepository) {

    suspend fun getAnimalListForUserAsync(user: User) {
        repository.getAnimalListForUserAsync(user)
    }
}