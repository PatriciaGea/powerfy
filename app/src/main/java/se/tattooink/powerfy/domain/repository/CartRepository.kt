package se.tattooink.powerfy.domain.repository

import kotlinx.coroutines.flow.Flow

data class CartLine(val productId: Int, val quantity: Int)

interface CartRepository {
    fun getCartLines(): Flow<List<CartLine>>
    suspend fun addToCart(productId: Int)
    suspend fun incrementQuantity(productId: Int)
    suspend fun decrementQuantity(productId: Int)
}