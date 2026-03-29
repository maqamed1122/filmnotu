package com.filmnot.data.repository

import com.filmnot.data.db.dao.WatchlistDao
import com.filmnot.data.db.entity.WatchlistEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: WatchlistDao
) {
    private val uid get() = auth.currentUser?.uid

    private fun userCollection() = uid?.let {
        firestore.collection("users").document(it).collection("watchlist")
    }

    suspend fun syncToCloud(entity: WatchlistEntity) {
        val col = userCollection() ?: return
        val data = mapOf(
            "id" to entity.id,
            "title" to entity.title,
            "posterPath" to (entity.posterPath ?: ""),
            "backdropPath" to (entity.backdropPath ?: ""),
            "overview" to (entity.overview ?: ""),
            "voteAverage" to entity.voteAverage,
            "releaseDate" to (entity.releaseDate ?: ""),
            "mediaType" to entity.mediaType,
            "isWatched" to entity.isWatched,
            "isFavorite" to entity.isFavorite,
            "addedAt" to entity.addedAt,
            "watchedAt" to (entity.watchedAt ?: 0L),
            "userRating" to (entity.userRating ?: 0f),
            "userNote" to (entity.userNote ?: "")
        )
        col.document(entity.id.toString()).set(data, SetOptions.merge()).await()
    }

    suspend fun deleteFromCloud(id: Int) {
        val col = userCollection() ?: return
        col.document(id.toString()).delete().await()
    }

    suspend fun syncFromCloud() {
        val col = userCollection() ?: return
        val snapshot = col.get().await()
        for (doc in snapshot.documents) {
            val d = doc.data ?: continue
            val entity = WatchlistEntity(
                id = (d["id"] as? Long)?.toInt() ?: continue,
                title = d["title"] as? String ?: continue,
                posterPath = (d["posterPath"] as? String)?.ifEmpty { null },
                backdropPath = (d["backdropPath"] as? String)?.ifEmpty { null },
                overview = (d["overview"] as? String)?.ifEmpty { null },
                voteAverage = (d["voteAverage"] as? Double) ?: 0.0,
                releaseDate = (d["releaseDate"] as? String)?.ifEmpty { null },
                mediaType = d["mediaType"] as? String ?: "MOVIE",
                isWatched = d["isWatched"] as? Boolean ?: false,
                isFavorite = d["isFavorite"] as? Boolean ?: false,
                addedAt = (d["addedAt"] as? Long) ?: System.currentTimeMillis(),
                watchedAt = (d["watchedAt"] as? Long)?.takeIf { it > 0 },
                userRating = (d["userRating"] as? Double)?.toFloat()?.takeIf { it > 0 },
                userNote = (d["userNote"] as? String)?.ifEmpty { null }
            )
            dao.insert(entity)
        }
    }
}
