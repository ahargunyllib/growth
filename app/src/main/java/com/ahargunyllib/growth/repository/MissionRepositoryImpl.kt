package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.Mission
import com.ahargunyllib.growth.model.MissionCompletion
import com.ahargunyllib.growth.model.MissionProgress
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MissionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : MissionRepository {

    override suspend fun getAllMyMission(): Resource<List<Mission>> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("missions")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val missions = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Mission::class.java)
            }

            Log.d("MissionRepository", "getAllMyMission: ${missions.size} missions")
            return Resource.Success(missions)
        } catch (e: Exception) {
            Log.e("MissionRepository", "getAllMyMission: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch missions")
        }
    }

    override suspend fun createMissionProgress(missionProgress: MissionProgress): Resource<Boolean> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val progressWithUser = missionProgress.copy(
                userId = userId,
                createdAt = System.currentTimeMillis().toString()
            )

            val docRef = firebaseFirestore.collection("mission_progress").document()
            val progressWithId = progressWithUser.copy(id = docRef.id)

            docRef.set(progressWithId).await()

            Log.d("MissionRepository", "createMissionProgress: $progressWithId")
            return Resource.Success(true)
        } catch (e: Exception) {
            Log.e("MissionRepository", "createMissionProgress: ${e.message}")
            return Resource.Error(e.message ?: "Failed to create mission progress")
        }
    }

    override suspend fun updateMissionProgress(missionProgress: MissionProgress): Resource<Boolean> {
        try {
            if (missionProgress.id.isEmpty()) {
                return Resource.Error("Mission progress ID is required")
            }

            firebaseFirestore.collection("mission_progress")
                .document(missionProgress.id)
                .set(missionProgress)
                .await()

            Log.d("MissionRepository", "updateMissionProgress: ${missionProgress.id}")
            return Resource.Success(true)
        } catch (e: Exception) {
            Log.e("MissionRepository", "updateMissionProgress: ${e.message}")
            return Resource.Error(e.message ?: "Failed to update mission progress")
        }
    }

    override suspend fun createMissionCompletion(missionCompletion: MissionCompletion): Resource<Boolean> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val completionWithUser = missionCompletion.copy(
                userId = userId,
                createdAt = System.currentTimeMillis().toString()
            )

            val docRef = firebaseFirestore.collection("mission_completions").document()
            val completionWithId = completionWithUser.copy(id = docRef.id)

            docRef.set(completionWithId).await()

            Log.d("MissionRepository", "createMissionCompletion: $completionWithId")
            return Resource.Success(true)
        } catch (e: Exception) {
            Log.e("MissionRepository", "createMissionCompletion: ${e.message}")
            return Resource.Error(e.message ?: "Failed to create mission completion")
        }
    }

    override suspend fun updateMissionCompletion(missionCompletion: MissionCompletion): Resource<Boolean> {
        try {
            if (missionCompletion.id.isEmpty()) {
                return Resource.Error("Mission completion ID is required")
            }

            firebaseFirestore.collection("mission_completions")
                .document(missionCompletion.id)
                .set(missionCompletion)
                .await()

            Log.d("MissionRepository", "updateMissionCompletion: ${missionCompletion.id}")
            return Resource.Success(true)
        } catch (e: Exception) {
            Log.e("MissionRepository", "updateMissionCompletion: ${e.message}")
            return Resource.Error(e.message ?: "Failed to update mission completion")
        }
    }
}
