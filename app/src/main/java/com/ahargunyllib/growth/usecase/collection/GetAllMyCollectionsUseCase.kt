package com.ahargunyllib.growth.usecase.collection

import com.ahargunyllib.growth.contract.CollectionRepository
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetAllMyCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(): Resource<List<Collection>> {
        return collectionRepository.getAllMyCollections()
    }
}