package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.utils.Resource

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<User>

    suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        name: String
    ): Resource<User>

    suspend fun signInWithGoogle(): Resource<User>

    suspend fun signUpWithGoogle(): Resource<User>

    suspend fun signOut()

    suspend fun getCurrentSession(): Resource<User?>
}