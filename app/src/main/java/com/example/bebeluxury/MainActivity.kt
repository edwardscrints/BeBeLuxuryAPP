package com.example.bebeluxury

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bebeluxury.activities.AboutActivity
import com.example.bebeluxury.activities.AddEditProductActivity
import com.example.bebeluxury.activities.ProductDetailActivity
import com.example.bebeluxury.adapter.ProductAdapter
import com.example.bebeluxury.data.AppDatabase
import com.example.bebeluxury.repository.ProductRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAddProduct: FloatingActionButton
    private lateinit var repository: ProductRepository
    
    companion object {
        private const val TAG = "MainActivity_Lifecycle"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "📱 onCreate() - Activity está siendo creada")
        setContentView(R.layout.activity_main)
        
        // Configurar Toolbar con menú
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        
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
        Log.d(TAG, "✅ onCreate() - Inicialización completada")
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "👀 onStart() - Activity está visible para el usuario")
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "▶️ onResume() - Activity en primer plano, usuario puede interactuar")
        observeProducts()
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "⏸️ onPause() - Activity perdiendo foco, otra Activity en primer plano")
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "⏹️ onStop() - Activity ya no es visible")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "💀 onDestroy() - Activity siendo destruida")
    }
    
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "🔄 onRestart() - Activity volviendo desde estado detenido")
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
