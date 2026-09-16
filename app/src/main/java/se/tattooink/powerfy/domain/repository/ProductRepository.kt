package se.tattooink.powerfy.domain.repository

import se.tattooink.powerfy.domain.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): Result<List<Product>>
    suspend fun getElectronicsProducts(): Result<List<Product>>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
}