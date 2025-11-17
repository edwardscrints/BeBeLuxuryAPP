package com.example.bebeluxury.data

import androidx.room.*
import com.example.bebeluxury.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Long): Product?
    
    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<Product>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
