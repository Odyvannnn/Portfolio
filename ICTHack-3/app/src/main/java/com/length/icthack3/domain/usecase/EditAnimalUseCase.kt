package com.length.icthack3.domain.usecase

import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.repository.AnimalRepository

class EditAnimalUseCase(private val repository: AnimalRepository) {

    suspend fun editAnimal(animal: Animal) {
        repository.editAnimal(animal)
    }
}