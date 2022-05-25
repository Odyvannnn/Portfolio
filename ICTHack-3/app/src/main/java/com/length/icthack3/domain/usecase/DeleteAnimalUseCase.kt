package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.repository.AnimalRepository

class DeleteAnimalUseCase(private val repository: AnimalRepository) {

    suspend fun deleteAnimal(animal: Animal) {
        repository.deleteAnimal(animal)
    }
}