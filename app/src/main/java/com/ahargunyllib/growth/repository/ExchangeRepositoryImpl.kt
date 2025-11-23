package com.ahargunyllib.growth.repository

import android.util.Log
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.contract.ExchangeRepository
import com.ahargunyllib.growth.model.ExchangeMethod
import com.ahargunyllib.growth.model.ExchangeMethodType
import com.ahargunyllib.growth.model.ExchangeStatus
import com.ahargunyllib.growth.model.ExchangeTransaction
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ExchangeRepository {

    companion object {
        private const val TAG = "ExchangeRepository"
    }

    override suspend fun getAvailableExchangeMethods(): Resource<List<ExchangeMethod>> {
        try {
            // For now, we'll return hardcoded exchange methods
            // In the future, this could be fetched from Firestore or a remote config
            val methods = listOf(
                ExchangeMethod(
                    id = "dana",
                    type = ExchangeMethodType.DANA,
                    name = "Dana",
                    description = "Tanpa potongan admin",
                    iconResId = R.drawable.ic_dana,
                    minAmount = 100,
                    maxAmount = 10000,
                    conversionRate = 100.0f,
                    adminFee = 0,
                    isActive = true
                ),
                ExchangeMethod(
                    id = "bri",
                    type = ExchangeMethodType.BRI,
                    name = "BRI",
                    description = "BRI Virtual Account",
                    iconResId = R.drawable.ic_bri,
                    minAmount = 100,
                    maxAmount = 10000,
                    conversionRate = 100.0f,
                    adminFee = 0,
                    isActive = true
                ),
                ExchangeMethod(
                    id = "mandiri",
                    type = ExchangeMethodType.MANDIRI,
                    name = "Mandiri",
                    description = "Mandiri Virtual Account",
                    iconResId = R.drawable.ic_mandiri,
                    minAmount = 100,
                    maxAmount = 10000,
                    conversionRate = 100.0f,
                    adminFee = 0,
                    isActive = true
                ),
                ExchangeMethod(
                    id = "bni",
                    type = ExchangeMethodType.BNI,
                    name = "BNI",
                    description = "BNI Virtual Account",
                    iconResId = R.drawable.ic_bni,
                    minAmount = 100,
                    maxAmount = 10000,
                    conversionRate = 100.0f,
                    adminFee = 0,
                    isActive = true
                ),
                ExchangeMethod(
                    id = "bca",
                    type = ExchangeMethodType.BCA,
                    name = "BCA",
                    description = "BCA Virtual Account",
                    iconResId = R.drawable.ic_bca,
                    minAmount = 100,
                    maxAmount = 10000,
                    conversionRate = 100.0f,
                    adminFee = 0,
                    isActive = true
                )
            )

            Log.d(TAG, "getAvailableExchangeMethods: ${methods.size} methods")
            return Resource.Success(methods)
        } catch (e: Exception) {
            Log.e(TAG, "getAvailableExchangeMethods: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch exchange methods")
        }
    }

    override suspend fun initiateExchange(transaction: ExchangeTransaction): Resource<String> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            if (transaction.userId != userId) {
                return Resource.Error("Cannot create exchange for another user")
            }

            // Validate transaction
            if (transaction.pointsExchanged <= 0) {
                return Resource.Error("Invalid exchange amount")
            }

            if (transaction.accountNumber.isBlank()) {
                return Resource.Error("Account number is required")
            }

            if (transaction.accountName.isBlank()) {
                return Resource.Error("Account name is required")
            }

            // Create transaction document
            val docRef = firebaseFirestore.collection("exchange_transactions").document()
            val transactionWithId = transaction.copy(
                id = docRef.id,
                createdAt = System.currentTimeMillis().toString(),
                status = ExchangeStatus.PENDING
            )

            // Save to Firestore
            val transactionMap = mapOf(
                "id" to transactionWithId.id,
                "userId" to transactionWithId.userId,
                "methodType" to transactionWithId.methodType.value,
                "accountNumber" to transactionWithId.accountNumber,
                "accountName" to transactionWithId.accountName,
                "pointsExchanged" to transactionWithId.pointsExchanged,
                "amountReceived" to transactionWithId.amountReceived,
                "adminFee" to transactionWithId.adminFee,
                "status" to transactionWithId.status.value,
                "createdAt" to transactionWithId.createdAt,
                "processedAt" to transactionWithId.processedAt,
                "notes" to transactionWithId.notes
            )

            docRef.set(transactionMap).await()

            Log.d(TAG, "initiateExchange: Transaction created with ID: ${docRef.id}")
            return Resource.Success(docRef.id)
        } catch (e: Exception) {
            Log.e(TAG, "initiateExchange: ${e.message}")
            return Resource.Error(e.message ?: "Failed to initiate exchange")
        }
    }

    override suspend fun getMyExchangeHistory(): Resource<List<ExchangeTransaction>> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val snapshot = firebaseFirestore.collection("exchange_transactions")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val transactions = snapshot.documents.mapNotNull { doc ->
                try {
                    ExchangeTransaction(
                        id = doc.getString("id") ?: "",
                        userId = doc.getString("userId") ?: "",
                        methodType = ExchangeMethodType.valueOf(
                            doc.getString("methodType")?.uppercase() ?: "DANA"
                        ),
                        accountNumber = doc.getString("accountNumber") ?: "",
                        accountName = doc.getString("accountName") ?: "",
                        pointsExchanged = doc.getLong("pointsExchanged")?.toInt() ?: 0,
                        amountReceived = doc.getLong("amountReceived")?.toInt() ?: 0,
                        adminFee = doc.getLong("adminFee")?.toInt() ?: 0,
                        status = ExchangeStatus.valueOf(
                            doc.getString("status")?.uppercase() ?: "PENDING"
                        ),
                        createdAt = doc.getString("createdAt") ?: "",
                        processedAt = doc.getString("processedAt") ?: "",
                        notes = doc.getString("notes") ?: ""
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing exchange transaction: ${e.message}")
                    null
                }
            }

            Log.d(TAG, "getMyExchangeHistory: ${transactions.size} transactions")
            return Resource.Success(transactions)
        } catch (e: Exception) {
            Log.e(TAG, "getMyExchangeHistory: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch exchange history")
        }
    }

    override suspend fun getExchangeById(id: String): Resource<ExchangeTransaction> {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("User not authenticated")

            val doc = firebaseFirestore.collection("exchange_transactions")
                .document(id)
                .get()
                .await()

            if (!doc.exists()) {
                return Resource.Error("Exchange transaction not found")
            }

            // Verify ownership
            if (doc.getString("userId") != userId) {
                return Resource.Error("Access denied")
            }

            val transaction = ExchangeTransaction(
                id = doc.getString("id") ?: "",
                userId = doc.getString("userId") ?: "",
                methodType = ExchangeMethodType.valueOf(
                    doc.getString("methodType")?.uppercase() ?: "DANA"
                ),
                accountNumber = doc.getString("accountNumber") ?: "",
                accountName = doc.getString("accountName") ?: "",
                pointsExchanged = doc.getLong("pointsExchanged")?.toInt() ?: 0,
                amountReceived = doc.getLong("amountReceived")?.toInt() ?: 0,
                adminFee = doc.getLong("adminFee")?.toInt() ?: 0,
                status = ExchangeStatus.valueOf(
                    doc.getString("status")?.uppercase() ?: "PENDING"
                ),
                createdAt = doc.getString("createdAt") ?: "",
                processedAt = doc.getString("processedAt") ?: "",
                notes = doc.getString("notes") ?: ""
            )

            Log.d(TAG, "getExchangeById: $transaction")
            return Resource.Success(transaction)
        } catch (e: Exception) {
            Log.e(TAG, "getExchangeById: ${e.message}")
            return Resource.Error(e.message ?: "Failed to fetch exchange transaction")
        }
    }
}
