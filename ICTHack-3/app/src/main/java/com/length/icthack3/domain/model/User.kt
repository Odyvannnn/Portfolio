package com.length.icthack3.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    @Exclude
    var id: String = "",

    val username: String = "",
    var balance: Double = 0.0,
    val token: String = "",
    val exchangeToken: String = "",
    val type: Int = TYPE_STUDENT
) : Parcelable {
    companion object {
        const val TABLE_NAME = "User"
        const val TABLE_ID = "id"
        const val TYPE_TEACHER = 1
        const val TYPE_STUDENT = 0
    }
}