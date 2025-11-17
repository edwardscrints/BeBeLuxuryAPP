package com.example.bebeluxury.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val imageUrl: String = "" // URL o nombre del recurso drawable
)
