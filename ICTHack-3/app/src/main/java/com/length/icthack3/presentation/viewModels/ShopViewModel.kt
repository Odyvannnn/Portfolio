package com.length.icthack3.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.length.icthack3.data.AnimalRepositoryImpl
import com.length.icthack3.data.UserRepositoryImpl
import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.usecase.AddAnimalUseCase
import com.length.icthack3.domain.usecase.EditUserUseCase
import com.length.icthack3.domain.usecase.GetUserUseCase
import com.length.icthack3.presentation.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopViewModel: ViewModel() {

    private val prefs = Application.prefs

    private val animalRep = AnimalRepositoryImpl(Firebase.firestore)
    private val userRep = UserRepositoryImpl(Firebase.firestore)

    private val addAnimalUseCase = AddAnimalUseCase(animalRep)
    private val getUserUseCase = GetUserUseCase(userRep)
    private val editUserUseCase = EditUserUseCase(userRep)

    val user = MutableLiveData<User>(prefs.getUser())

    fun buyAnimal(animalType: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userAnimal = Animal(ownerId = user.value!!.id, type = animalType, name = "Pig")
            if (user.value?.balance!! >= userAnimal.cost){
                addAnimalUseCase.addAnimal(userAnimal)
                user.value!!.balance = user.value!!.balance - userAnimal.cost
                editUserUseCase.editUser(user.value!!)
                user.postValue(getUserUseCase.getUser(user.value!!.token))
            }
        }
    }

    fun updateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.getUser(user.value!!.id)
        }
    }
}