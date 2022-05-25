package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.repository.AnimalRepository

class AddAnimalUseCase(private val repository: AnimalRepository) {

    suspend fun addAnimal(animal: Animal) {
        repository.addAnimal(animal)
    }
}