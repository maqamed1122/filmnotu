package com.filmnot.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.filmnot.data.model.MediaType

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String?,
    val voteAverage: Double,
    val releaseDate: String?,
    val mediaType: String, // "MOVIE" or "TV"
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis(),
    val watchedAt: Long? = null,
    val userRating: Float? = null,
    val userNote: String? = null
) {
    fun posterUrl() = if (posterPath != null) "https://image.tmdb.org/t/p/w500$posterPath" else null
    fun backdropUrl() = if (backdropPath != null) "https://image.tmdb.org/t/p/w780$backdropPath" else null
    fun type() = if (mediaType == "TV") MediaType.TV else MediaType.MOVIE
}
