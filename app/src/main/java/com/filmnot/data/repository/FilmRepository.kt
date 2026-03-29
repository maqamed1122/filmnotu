package com.filmnot.data.repository

import com.filmnot.data.api.TmdbApiService
import com.filmnot.data.db.dao.WatchlistDao
import com.filmnot.data.db.entity.WatchlistEntity
import com.filmnot.data.model.MediaType
import com.filmnot.data.model.TmdbMovie
import com.filmnot.data.model.TmdbMovieDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmRepository @Inject constructor(
    private val api: TmdbApiService,
    private val dao: WatchlistDao,
    private val firestore: FirestoreRepository
) {
    // API
    suspend fun getTrending() = api.getTrending()
    suspend fun getPopularMovies(page: Int = 1) = api.getPopularMovies(page)
    suspend fun getPopularTv(page: Int = 1) = api.getPopularTv(page)
    suspend fun getTopRatedMovies() = api.getTopRatedMovies()
    suspend fun getTopRatedTv() = api.getTopRatedTv()
    suspend fun getUpcoming() = api.getUpcomingMovies()
    suspend fun getNowPlaying() = api.getNowPlaying()
    suspend fun getMovieDetail(id: Int) = api.getMovieDetail(id)
    suspend fun getTvDetail(id: Int) = api.getTvDetail(id)
    suspend fun search(query: String) = api.searchMulti(query)

    // Watchlist - local
    fun getAllWatchlist() = dao.getAllWatchlist()
    fun getUnwatched() = dao.getUnwatched()
    fun getWatched() = dao.getWatched()
    fun getFavorites() = dao.getFavorites()
    fun isInWatchlist(id: Int) = dao.isInWatchlist(id)
    fun getById(id: Int) = dao.getById(id)
    fun getTotalCount() = dao.getTotalCount()
    fun getWatchedCount() = dao.getWatchedCount()
    fun getMovieCount() = dao.getMovieCount()
    fun getTvCount() = dao.getTvCount()

    suspend fun addDetailToWatchlist(detail: TmdbMovieDetail, mediaType: MediaType) {
        val entity = WatchlistEntity(
            id = detail.id,
            title = detail.displayTitle(),
            posterPath = detail.posterPath,
            backdropPath = detail.backdropPath,
            overview = detail.overview,
            voteAverage = detail.voteAverage,
            releaseDate = detail.displayDate(),
            mediaType = mediaType.name
        )
        dao.insert(entity)
        try { firestore.syncToCloud(entity) } catch (_: Exception) {}
    }

    suspend fun removeFromWatchlist(id: Int) {
        dao.deleteById(id)
        try { firestore.deleteFromCloud(id) } catch (_: Exception) {}
    }

    suspend fun setWatched(id: Int, watched: Boolean) {
        val time = if (watched) System.currentTimeMillis() else null
        dao.setWatched(id, watched, time)
    }

    suspend fun setFavorite(id: Int, favorite: Boolean) {
        dao.setFavorite(id, favorite)
    }

    suspend fun setRating(id: Int, rating: Float?) = dao.setRating(id, rating)
    suspend fun setNote(id: Int, note: String?) = dao.setNote(id, note)
}
