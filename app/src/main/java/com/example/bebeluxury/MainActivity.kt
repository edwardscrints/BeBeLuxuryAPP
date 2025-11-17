package com.example.bebeluxury

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bebeluxury.activities.AddEditProductActivity
import com.example.bebeluxury.activities.ProductDetailActivity
import com.example.bebeluxury.adapter.ProductAdapter
import com.example.bebeluxury.data.AppDatabase
import com.example.bebeluxury.repository.ProductRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAddProduct: FloatingActionButton
    private lateinit var repository: ProductRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Inicializar Room Database
        val database = AppDatabase.getDatabase(this)
        repository = ProductRepository(database.productDao())
        
        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = ProductAdapter { product ->
            // Click en un producto
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        
        recyclerView.adapter = adapter
        
        // Configurar FAB
        fabAddProduct = findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            val intent = Intent(this, AddEditProductActivity::class.java)
            startActivity(intent)
        }
        
        // Observar productos
        observeProducts()
    }
    
    override fun onResume() {
        super.onResume()
        observeProducts()
    }
    
    private fun observeProducts() {
        lifecycleScope.launch {
            repository.allProducts.collect { products ->
                adapter.submitList(products)
                
                val emptyView = findViewById<View>(R.id.textViewEmpty)
                if (products.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
            }
        }
    }
}
