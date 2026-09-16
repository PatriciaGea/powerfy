package se.tattooink.powerfy.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import se.tattooink.powerfy.data.remote.api.ProductApi
import se.tattooink.powerfy.data.remote.dto.ProductDto
import se.tattooink.powerfy.domain.model.Product
import se.tattooink.powerfy.domain.repository.ProductRepository
import javax.inject.Inject

private val ELECTRONICS_CATEGORIES = listOf(
    "smartphones",
    "laptops",
    "tablets",
    "mobile-accessories"
)

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {

    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = productApi.getAllProducts()
            Result.success(response.products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getElectronicsProducts(): Result<List<Product>> {
        return try {
            val products = coroutineScope {
                val deferredResults = ELECTRONICS_CATEGORIES.map { category ->
                    async { productApi.getProductsByCategory(category).products }
                }
                deferredResults.flatMap { it.await() }
            }
            Result.success(products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val response = productApi.getProductsByCategory(category)
            Result.success(response.products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = productApi.searchProducts(query)
            Result.success(response.products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val dto = productApi.getProductById(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        imageUrl = thumbnail,
        category = category
    )
}