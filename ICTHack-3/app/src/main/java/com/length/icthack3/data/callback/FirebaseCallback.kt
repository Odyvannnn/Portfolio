package com.length.icthack3.data.callback

interface FirebaseCallback {

    fun onError(throwable: Throwable) { }

    fun onSuccess() { }
}