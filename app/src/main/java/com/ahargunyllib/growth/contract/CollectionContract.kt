package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.utils.Resource

interface CollectionRepository {
    suspend fun createCollection(collection: Collection): Resource<Collection>
    suspend fun getAllMyCollections(): Resource<List<Collection>>
}