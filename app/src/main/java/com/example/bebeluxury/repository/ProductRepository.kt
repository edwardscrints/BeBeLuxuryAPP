package com.example.bebeluxury.repository

import com.example.bebeluxury.data.ProductDao
import com.example.bebeluxury.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    
    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }
    
    suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)
    }
    
    suspend fun insert(product: Product): Long {
        return productDao.insertProduct(product)
    }
    
    suspend fun update(product: Product) {
        productDao.updateProduct(product)
    }
    
    suspend fun delete(product: Product) {
        productDao.deleteProduct(product)
    }
    
    suspend fun deleteAll() {
        productDao.deleteAllProducts()
    }
}
