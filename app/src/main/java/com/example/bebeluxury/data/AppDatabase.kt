package com.example.bebeluxury.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bebeluxury.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun productDao(): ProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bebeluxury_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Datos de ejemplo al crear la BD
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.productDao())
                    }
                }
            }
        }
        
        suspend fun populateDatabase(productDao: ProductDao) {
            // Datos de ejemplo
            val products = listOf(
                Product(
                    name = "Bolso de Lujo",
                    description = "Elegante bolso de cuero genuino",
                    price = 299.99,
                    category = "Bolsos",
                    stock = 15,
                    imageUrl = ""
                ),
                Product(
                    name = "Reloj Premium",
                    description = "Reloj de oro con diamantes",
                    price = 1299.99,
                    category = "Relojes",
                    stock = 8,
                    imageUrl = ""
                ),
                Product(
                    name = "Perfume Exclusivo",
                    description = "Fragancia de diseñador",
                    price = 149.99,
                    category = "Perfumes",
                    stock = 25,
                    imageUrl = ""
                ),
                Product(
                    name = "Zapatos Elegantes",
                    description = "Zapatos de tacón alto de diseñador",
                    price = 399.99,
                    category = "Calzado",
                    stock = 12,
                    imageUrl = ""
                ),
                Product(
                    name = "Collar de Perlas",
                    description = "Collar de perlas naturales",
                    price = 599.99,
                    category = "Joyería",
                    stock = 5,
                    imageUrl = ""
                )
            )
            
            products.forEach { product ->
                productDao.insertProduct(product)
            }
        }
    }
}
