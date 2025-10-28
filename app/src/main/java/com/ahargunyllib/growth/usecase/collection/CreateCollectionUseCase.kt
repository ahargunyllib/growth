package com.ahargunyllib.growth.usecase.collection

import com.ahargunyllib.growth.contract.CollectionRepository
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.model.CollectionStatus
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class CreateCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(
        partnerId: String,
        totalWeightKg: Int,
        receivedPoints: Int
    ): Resource<Collection> {
        if (partnerId.isBlank()) {
            return Resource.Error("Partner ID tidak boleh kosong")
        }

        if (totalWeightKg <= 0) {
            return Resource.Error("Berat sampah harus lebih dari 0")
        }

        if (receivedPoints < 0) {
            return Resource.Error("Poin tidak valid")
        }

        val collection = Collection(
            partnerId = partnerId,
            totalWeightKg = totalWeightKg,
            receivedPoints = receivedPoints,
            status = CollectionStatus.SUCCESS
        )

        return collectionRepository.createCollection(collection)
    }
}
