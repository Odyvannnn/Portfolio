package com.length.icthack3.domain.repository

import androidx.lifecycle.LiveData
import com.length.icthack3.domain.model.User

interface UserRepository {

    fun getUserList(): LiveData<List<User>>

    suspend fun addUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun editUser(user: User)

    suspend fun getUser(userId: String): User?

    suspend fun getUserListAsync()
}