package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.AnimalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class GetAnimalListForUserUseCase(private val repository: AnimalRepository) {

    fun getAnimalListForUser() {
        repository.getAnimalListForUser()
    }
}