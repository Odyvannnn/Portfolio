package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.repository.AnimalRepository

class GetAnimalUseCase(private val repository: AnimalRepository) {

    suspend fun getAnimal(animalId: String) {
        repository.getAnimal(animalId)
    }
}