package se.tattooink.powerfy.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import se.tattooink.powerfy.data.local.CartDao
import se.tattooink.powerfy.data.local.CartItemEntity
import se.tattooink.powerfy.domain.repository.CartLine
import se.tattooink.powerfy.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartLines(): Flow<List<CartLine>> {
        return cartDao.getAllCartItems()
            .onEach { entities -> Log.d("CartDebug", "Cart flow emitted: $entities") }
            .map { entities ->
                entities.map { CartLine(productId = it.productId, quantity = it.quantity) }
            }
    }

    override suspend fun addToCart(productId: Int) {
        Log.d("CartDebug", "addToCart called for productId=$productId")
        val existingQuantity = cartDao.getQuantity(productId) ?: 0
        cartDao.upsert(CartItemEntity(productId = productId, quantity = existingQuantity + 1))
    }

    override suspend fun incrementQuantity(productId: Int) {
        val currentQuantity = cartDao.getQuantity(productId) ?: 0
        cartDao.upsert(CartItemEntity(productId = productId, quantity = currentQuantity + 1))
    }

    override suspend fun decrementQuantity(productId: Int) {
        val currentQuantity = cartDao.getQuantity(productId) ?: 0
        if (currentQuantity <= 1) {
            cartDao.remove(productId)
        } else {
            cartDao.upsert(CartItemEntity(productId = productId, quantity = currentQuantity - 1))
        }
    }
}