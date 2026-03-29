package com.filmnot.data.db.dao

import androidx.room.*
import com.filmnot.data.db.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE isWatched = 0 ORDER BY addedAt DESC")
    fun getUnwatched(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE isWatched = 1 ORDER BY watchedAt DESC")
    fun getWatched(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE isFavorite = 1 ORDER BY addedAt DESC")
    fun getFavorites(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE id = :id")
    fun getById(id: Int): Flow<WatchlistEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :id)")
    fun isInWatchlist(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchlistEntity)

    @Update
    suspend fun update(entity: WatchlistEntity)

    @Delete
    suspend fun delete(entity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE watchlist SET isWatched = :watched, watchedAt = :time WHERE id = :id")
    suspend fun setWatched(id: Int, watched: Boolean, time: Long?)

    @Query("UPDATE watchlist SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Int, favorite: Boolean)

    @Query("UPDATE watchlist SET userRating = :rating WHERE id = :id")
    suspend fun setRating(id: Int, rating: Float?)

    @Query("UPDATE watchlist SET userNote = :note WHERE id = :id")
    suspend fun setNote(id: Int, note: String?)

    @Query("SELECT COUNT(*) FROM watchlist")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM watchlist WHERE isWatched = 1")
    fun getWatchedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM watchlist WHERE mediaType = 'MOVIE'")
    fun getMovieCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM watchlist WHERE mediaType = 'TV'")
    fun getTvCount(): Flow<Int>
}
