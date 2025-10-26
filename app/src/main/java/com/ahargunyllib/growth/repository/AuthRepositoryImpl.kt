package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.contract.AuthRepository
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): AuthRepository {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<User> {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser == null) {
                return Resource.Error<User>("Masuk Gagal")
            }

            val userSnapshot = firebaseFirestore.collection("users").document(firebaseUser.uid).get().await()
            if (!userSnapshot.exists()) {
                return Resource.Error<User>("Masuk Gagal")
            }

            val user = userSnapshot.toObject(User::class.java)

            Log.d("AuthRepositoryImpl", "signInWithEmailAndPassword: ${user}" )

            return Resource.Success(user)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signInWithEmailAndPassword: ${e.message}" )

            return Resource.Error(e.message ?: "Masuk Gagal")
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        name: String
    ): Resource<User> {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser == null) {
                return Resource.Error<User>("Registrasi Gagal")
            }

            val user = User(
                id = firebaseUser.uid,
                name = name,
                email = email,
                profileUrl = null,
                gender = null,
                phoneNumber = null,
                createdAt = System.currentTimeMillis(),
            )

            firebaseFirestore.collection("users").document(user.id).set(user).await()

            return Resource.Success(user)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Registrasi Gagal")
        }
    }

    override suspend fun signInWithGoogle(): Resource<User> {
        TODO("Not yet implemented")
    }

    override suspend fun signUpWithGoogle(): Resource<User> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentSession(): Resource<User?> {
        val firebaseUser = firebaseAuth.currentUser ?: return Resource.Success(null)

        try {
            val userSnapshot = firebaseFirestore.collection("users").document(firebaseUser.uid).get().await()
            if (!userSnapshot.exists()) {
                return Resource.Success(null)
            }

            val user = userSnapshot.toObject(User::class.java)
            return Resource.Success(user)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Gagal mengambil sesi")
        }
    }
}