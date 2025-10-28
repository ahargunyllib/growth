package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.contract.PointRepository
import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.model.PointPosting
import com.ahargunyllib.growth.model.PointPostingRefType
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PointRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : PointRepository {

    override suspend fun getMyPointAccount(): Resource<PointAccount> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("point_accounts")
                .whereEqualTo("ownerId", userId)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                // Create new point account if doesn't exist
                val newAccount = PointAccount(
                    ownerId = userId,
                    balance = 0
                )
                val docRef = firebaseFirestore.collection("point_accounts").document(userId)
                docRef.set(newAccount).await()

                Log.d("PointRepository", "getMyPointAccount: Created new account")
                return Resource.Success(newAccount)
            }

            val pointAccount = snapshot.documents.first().toObject(PointAccount::class.java)
                ?: return Resource.Error("Failed to parse point account data")

            Log.d("PointRepository", "getMyPointAccount: $pointAccount")
            return Resource.Success(pointAccount)
        } catch (e: Exception) {
            Log.e("PointRepository", "getMyPointAccount: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch point account")
        }
    }

    override suspend fun updateMyPointAccount(pointAccount: PointAccount): Resource<Boolean> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            if (pointAccount.ownerId != userId) {
                return Resource.Error("Cannot update another user's point account")
            }

            firebaseFirestore.collection("point_accounts")
                .document(userId)
                .set(pointAccount)
                .await()

            Log.d("PointRepository", "updateMyPointAccount: Success")
            return Resource.Success(true)
        } catch (e: Exception) {
            Log.e("PointRepository", "updateMyPointAccount: ${e.message}")
            return Resource.Error(e.message ?: "Failed to update point account")
        }
    }

    override suspend fun getAllMyPointPosting(): Resource<List<PointPosting>> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("point_postings")
                .whereEqualTo("accountId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val postings = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PointPosting::class.java)?.copy(
                    refType = PointPostingRefType.valueOf(
                        doc.getString("refType")?.uppercase()?.replace(" ", "_") ?: "DEPOSIT"
                    )
                )
            }

            Log.d("PointRepository", "getAllMyPointPosting: ${postings.size} postings")
            return Resource.Success(postings)
        } catch (e: Exception) {
            Log.e("PointRepository", "getAllMyPointPosting: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch point postings")
        }
    }

    override suspend fun createPointPosting(pointPosting: PointPosting): Resource<Boolean> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val postingWithUser = pointPosting.copy(
                accountId = userId,
                createdAt = System.currentTimeMillis().toString()
            )

            val docRef = firebaseFirestore.collection("point_postings").document()
            val postingWithId = postingWithUser.copy(id = docRef.id)

            docRef.set(postingWithId).await()

            Log.d("PointRepository", "createPointPosting: $postingWithId")
            return Resource.Success(true)
        } catch (e: Exception) {
            Log.e("PointRepository", "createPointPosting: ${e.message}")
            return Resource.Error(e.message ?: "Failed to create point posting")
        }
    }
}
