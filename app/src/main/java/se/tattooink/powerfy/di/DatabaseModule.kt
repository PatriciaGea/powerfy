package se.tattooink.powerfy.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import se.tattooink.powerfy.data.local.CartDao
import se.tattooink.powerfy.data.local.FavoriteDao
import se.tattooink.powerfy.data.local.MIGRATION_1_2
import se.tattooink.powerfy.data.local.PowerfyDatabase
import se.tattooink.powerfy.data.repository.CartRepositoryImpl
import se.tattooink.powerfy.data.repository.FavoriteRepositoryImpl
import se.tattooink.powerfy.domain.repository.CartRepository
import se.tattooink.powerfy.domain.repository.FavoriteRepository
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
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: PowerfyDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: PowerfyDatabase): CartDao {
        return database.cartDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRepositoryModule {

    @Binds
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}