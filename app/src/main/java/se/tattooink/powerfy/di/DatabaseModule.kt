package se.tattooink.powerfy.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import se.tattooink.powerfy.data.local.FavoriteDao
import se.tattooink.powerfy.data.local.PowerfyDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePowerfyDatabase(@ApplicationContext context: Context): PowerfyDatabase {
        return Room.databaseBuilder(
            context,
            PowerfyDatabase::class.java,
            "powerfy_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: PowerfyDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}
@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRepositoryModule {

    @Binds
    abstract fun bindFavoriteRepository(impl: se.tattooink.powerfy.data.repository.FavoriteRepositoryImpl): se.tattooink.powerfy.domain.repository.FavoriteRepository
}