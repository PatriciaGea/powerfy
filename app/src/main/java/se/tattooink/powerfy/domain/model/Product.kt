package se.tattooink.powerfy.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val imageUrl: String,
    val category: String
)