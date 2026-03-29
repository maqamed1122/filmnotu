package com.filmnot.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.filmnot.data.db.dao.WatchlistDao
import com.filmnot.data.db.entity.WatchlistEntity

@Database(
    entities = [WatchlistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FilmnotDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}
