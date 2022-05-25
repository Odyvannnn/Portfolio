package com.length.icthack3.presentation.util

fun randomId(): String = List(16) {
    (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
}.joinToString("")