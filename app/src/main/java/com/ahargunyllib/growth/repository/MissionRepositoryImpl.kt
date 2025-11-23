package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.Mission
import com.ahargunyllib.growth.model.MissionCompletion
import com.ahargunyllib.growth.model.MissionProgress
import com.ahargunyllib.growth.model.MissionWithProgress
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
                .get()
                .await()

            val missions = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Mission::class.java)?.copy(id = doc.id)
            }

            Log.d("MissionRepository", "getAllMyMission: ${missions.size} missions")
            return Resource.Success(missions)
        } catch (e: Exception) {
            Log.e("MissionRepository", "getAllMyMission: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch missions")
        }
    }

    override suspend fun getAllMyMissionsWithProgress(): Resource<List<MissionWithProgress>> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            // Fetch all missions
            val missionsSnapshot = firebaseFirestore.collection("missions")
                .get()
                .await()

            val missions = missionsSnapshot.documents.mapNotNull { doc ->
                doc.toObject(Mission::class.java)?.copy(id = doc.id)
            }

            // Fetch all progress for this user
            val progressSnapshot = firebaseFirestore.collection("mission_progress")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val progressMap = progressSnapshot.documents.mapNotNull { doc ->
                doc.toObject(MissionProgress::class.java)?.copy(id = doc.id)
            }.associateBy { it.missionId }

            // Fetch all completions for this user
            val completionsSnapshot = firebaseFirestore.collection("mission_completions")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val completionsMap = completionsSnapshot.documents.mapNotNull { doc ->
                doc.toObject(MissionCompletion::class.java)?.copy(id = doc.id)
            }.associateBy { it.missionId }

            // Combine data
            val missionsWithProgress = missions.map { mission ->
                MissionWithProgress(
                    mission = mission,
                    progress = progressMap[mission.id],
                    completion = completionsMap[mission.id]
                )
            }

            Log.d("MissionRepository", "getAllMyMissionsWithProgress: ${missionsWithProgress.size} missions")
            return Resource.Success(missionsWithProgress)
        } catch (e: Exception) {
            Log.e("MissionRepository", "getAllMyMissionsWithProgress: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch missions with progress")
        }
    }

    override suspend fun getMissionWithProgress(missionId: String): Resource<MissionWithProgress?> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            // Validate missionId
            if (missionId.isEmpty()) {
                return Resource.Error("Mission ID is empty")
            }

            // Fetch the specific mission
            val missionSnapshot = firebaseFirestore.collection("missions")
                .document(missionId)
                .get()
                .await()

            val mission = missionSnapshot.toObject(Mission::class.java)?.copy(id = missionSnapshot.id)
                ?: return Resource.Error("Mission not found")

            // Fetch progress for this user and mission
            val progressSnapshot = firebaseFirestore.collection("mission_progress")
                .whereEqualTo("userId", userId)
                .whereEqualTo("missionId", missionId)
                .limit(1)
                .get()
                .await()

            val progress = progressSnapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(MissionProgress::class.java)?.copy(id = doc.id)
            }

            // Fetch completion for this user and mission
            val completionSnapshot = firebaseFirestore.collection("mission_completions")
                .whereEqualTo("userId", userId)
                .whereEqualTo("missionId", missionId)
                .limit(1)
                .get()
                .await()

            val completion = completionSnapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(MissionCompletion::class.java)?.copy(id = doc.id)
            }

            // Combine data
            val missionWithProgress = MissionWithProgress(
                mission = mission,
                progress = progress,
                completion = completion
            )

            Log.d("MissionRepository", "getMissionWithProgress: $missionWithProgress")
            return Resource.Success(missionWithProgress)
        } catch (e: Exception) {
            Log.e("MissionRepository", "getMissionWithProgress: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch mission with progress")
        }
    }

    override suspend fun getMissionProgress(missionId: String): Resource<MissionProgress?> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("mission_progress")
                .whereEqualTo("userId", userId)
                .whereEqualTo("missionId", missionId)
                .limit(1)
                .get()
                .await()

            val progress = snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(MissionProgress::class.java)?.copy(id = doc.id)
            }

            Log.d("MissionRepository", "getMissionProgress: $progress")
            return Resource.Success(progress)
        } catch (e: Exception) {
            Log.e("MissionRepository", "getMissionProgress: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch mission progress")
        }
    }

    override suspend fun createMissionProgress(missionProgress: MissionProgress): Resource<Boolean> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val progressWithUser = missionProgress.copy(
                userId = userId,
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

    override suspend fun getMissionCompletion(missionId: String): Resource<MissionCompletion?> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("mission_completions")
                .whereEqualTo("userId", userId)
                .whereEqualTo("missionId", missionId)
                .limit(1)
                .get()
                .await()

            val completion = snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(MissionCompletion::class.java)?.copy(id = doc.id)
            }

            Log.d("MissionRepository", "getMissionCompletion: $completion")
            return Resource.Success(completion)
        } catch (e: Exception) {
            Log.e("MissionRepository", "getMissionCompletion: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch mission completion")
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
