package com.ahargunyllib.growth.model

import android.net.Uri

enum class Gender (
    val value: String,
    val label: String
){
    MALE("male", "Laki - Laki"),
    FEMALE("female", "Perempuan"),
    OTHER("other", "Lainnya")
}

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val profileUrl: String? = null,
    val gender: Gender? = null,
    val phoneNumber: String? = null,
    val createdAt: Long = 0L,
)