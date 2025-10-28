package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.contract.PartnerRepository
import com.ahargunyllib.growth.model.Partner
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PartnerRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : PartnerRepository {

    override suspend fun getAllPartners(): Resource<List<Partner>> {
        try {
            val snapshot = firebaseFirestore.collection("partners")
                .get()
                .await()

            val partners = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Partner::class.java)
            }

            Log.d("PartnerRepository", "getAllPartners: ${partners.size} partners")
            return Resource.Success(partners)
        } catch (e: Exception) {
            Log.e("PartnerRepository", "getAllPartners: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch partners")
        }
    }

    override suspend fun getPartnerById(id: String): Resource<Partner> {
        try {
            if (id.isEmpty()) {
                return Resource.Error("Partner ID is required")
            }

            val snapshot = firebaseFirestore.collection("partners")
                .document(id)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Resource.Error("Partner not found")
            }

            val partner = snapshot.toObject(Partner::class.java)
                ?: return Resource.Error("Failed to parse partner data")

            Log.d("PartnerRepository", "getPartnerById: $partner")
            return Resource.Success(partner)
        } catch (e: Exception) {
            Log.e("PartnerRepository", "getPartnerById: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch partner")
        }
    }
}
