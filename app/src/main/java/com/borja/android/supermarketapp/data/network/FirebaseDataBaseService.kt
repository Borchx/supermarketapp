package com.borja.android.supermarketapp.data.network

import android.net.Uri
import android.util.Log
import com.borja.android.supermarketapp.data.response.ProductResponse
import com.borja.android.supermarketapp.data.response.TopProductsResponse
import com.borja.android.supermarketapp.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseDataBaseService @Inject constructor(
    private val firestore: FirebaseFirestore, private val storage: FirebaseStorage
) {

    companion object {
        const val PRODUCTS_PATH = "products"
        const val MANAGEMENTS_PATH = "management"
        const val TOP_PRODUCT_DOCUMENT = "top_products"
    }

    suspend fun getAllProducts(): List<Product> {
        return firestore.collection(PRODUCTS_PATH).get().await().map { product ->
            product.toObject(ProductResponse::class.java).toDomain()
        }
    }

    suspend fun getLastProduct(): Product? {
        return firestore.collection(PRODUCTS_PATH).orderBy("id", Query.Direction.DESCENDING)
            .limit(1).get().await().firstOrNull()?.toObject(ProductResponse::class.java)?.toDomain()
    }

    suspend fun getTopProducts(): List<String> {
        return firestore.collection(MANAGEMENTS_PATH).document(TOP_PRODUCT_DOCUMENT).get().await()
            .toObject(TopProductsResponse::class.java)?.ids ?: emptyList()
    }

    suspend fun uploadAndDownloadImage(uri: Uri): String {
        return suspendCancellableCoroutine<String> { suspendCancellable ->
            val refence = storage.reference.child("download/${uri.lastPathSegment}")
            refence.putFile(uri, createMetaData()).addOnSuccessListener {
                downloadImage(it, suspendCancellable)
            }.addOnFailureListener {
                suspendCancellable.resume("")
            }
        }
    }

    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot, suspendCancellable: CancellableContinuation<String>
    ) {
        uploadTask.storage.downloadUrl.addOnSuccessListener {
            suspendCancellable.resume(it.toString())
        }.addOnFailureListener {
            suspendCancellable.resume("")
        }
    }

    private fun createMetaData(): StorageMetadata {
        val metadata = storageMetadata {
            contentType = "image/jpeg"
            setCustomMetadata("date", Date().time.toString())
        }
        return metadata
    }

    private fun generateProductId(): String {
        return Date().time.toString()
    }

    suspend fun uploadNewProduct(
        name: String,
        description: String,
        price: String,
        imageUrl: String
    ): Boolean {
        val id = generateProductId()
        val product = hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "price" to price,
            "image" to imageUrl
        )

        return suspendCancellableCoroutine { cancellableCoroutine ->
            firestore.collection(PRODUCTS_PATH).document(id).set(product).addOnCompleteListener {
                cancellableCoroutine.resume(true)
            }.addOnFailureListener {
                cancellableCoroutine.resume(false)
            }
        }

    }
}

