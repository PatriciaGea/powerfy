package se.tattooink.powerfy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class PowerfyDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}