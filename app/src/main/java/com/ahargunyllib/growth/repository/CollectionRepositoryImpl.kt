package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.contract.CollectionRepository
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.model.CollectionStatus
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : CollectionRepository {

    override suspend fun createCollection(collection: Collection): Resource<Collection> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val collectionWithUser = collection.copy(
                userId = userId,
                createdAt = System.currentTimeMillis().toString()
            )

            val docRef = firebaseFirestore.collection("collections").document()
            val collectionWithId = collectionWithUser.copy(id = docRef.id)

            docRef.set(collectionWithId).await()

            Log.d("CollectionRepository", "createCollection: $collectionWithId")
            return Resource.Success(collectionWithId)
        } catch (e: Exception) {
            Log.e("CollectionRepository", "createCollection: ${e.message}")
            return Resource.Error(e.message ?: "Failed to create collection")
        }
    }

    override suspend fun getAllMyCollections(): Resource<List<Collection>> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("collections")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val collections = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Collection::class.java)?.copy(
                    status = CollectionStatus.valueOf(
                        doc.getString("status")?.uppercase() ?: "PENDING"
                    )
                )
            }

            Log.d("CollectionRepository", "getAllMyCollections: ${collections.size} collections")
            return Resource.Success(collections)
        } catch (e: Exception) {
            Log.e("CollectionRepository", "getAllMyCollections: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch collections")
        }
    }
}
