package se.tattooink.powerfy.data.repository

import se.tattooink.powerfy.data.local.FavoriteDao
import se.tattooink.powerfy.data.local.FavoriteEntity
import se.tattooink.powerfy.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavoriteIds(): Flow<List<Int>> {
        return favoriteDao.getAllFavoriteIds()
    }

    override suspend fun toggleFavorite(productId: Int) {
        val isFavorite = favoriteDao.isFavorite(productId)
        if (isFavorite) {
            favoriteDao.removeFavorite(productId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(productId))
        }
    }
}