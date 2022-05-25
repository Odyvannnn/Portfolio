package com.length.icthack3.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.length.icthack3.domain.model.User
import com.length.icthack3.domain.repository.UserRepository
import com.length.icthack3.presentation.Application
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val db: FirebaseFirestore) : UserRepository {

    private val userList = MutableLiveData<List<User>>()

    private val userListSorted = sortedSetOf<User>({ p0, p1 -> p1.balance.compareTo(p0.balance) })

    private val prefs = Application.prefs

    override suspend fun getUser(userId: String): User? {
        try {
            return db.collection(User.TABLE_NAME)
                .document(userId)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    user?.id = userId
                    if (it.exists() && user != null) prefs.saveAndEditUser(user)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .await()
                .toObject(User::class.java)
        } catch (t: IllegalArgumentException) {
            return null
        }
    }

    override fun getUserList(): LiveData<List<User>> {
        return userList
    }

    override suspend fun addUser(user: User) {
        db.collection(User.TABLE_NAME)
            .add(user)
            .addOnSuccessListener {
                userListSorted.add(user)
                updateList()
                user.id = it.id
                prefs.saveAndEditUser(user)
            }
            .await()
    }

    override suspend fun deleteUser(user: User) {
        db.collection(User.TABLE_NAME)
            .document(user.id)
            .delete()
            .addOnSuccessListener {
                userListSorted.remove(user)
                updateList()
            }
            .await()
    }

    override suspend fun editUser(user: User) {
        db.collection(User.TABLE_NAME)
            .document(user.token)
            .set(user)
            .addOnSuccessListener {
                val oldUser = userListSorted.find { it.id == user.id }
                userListSorted.remove(oldUser)
                userListSorted.add(user)
                updateList()
                prefs.saveAndEditUser(user)
            }
            .await()
    }

    override suspend fun getUserListAsync() {
        val users = db.collection(User.TABLE_NAME)
            .get()
            .addOnFailureListener {
                it.printStackTrace()
            }
            .await()
            .toObjects(User::class.java)
        userListSorted.addAll(users)
        updateList()
    }

    private fun updateList() {
        userList.postValue(userListSorted.toList())
    }
}