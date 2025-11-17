package com.example.bebeluxury.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bebeluxury.R
import com.example.bebeluxury.data.AppDatabase
import com.example.bebeluxury.model.Product
import com.example.bebeluxury.repository.ProductRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import android.content.Intent
import android.widget.Toast

class ProductDetailActivity : AppCompatActivity() {
    
    private lateinit var repository: ProductRepository
    private var currentProduct: Product? = null
    
    companion object {
        private const val TAG = "ProductDetail_Lifecycle"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "📱 onCreate() - Activity de detalle creada")
        setContentView(R.layout.activity_product_detail)
        
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val database = AppDatabase.getDatabase(this)
        repository = ProductRepository(database.productDao())
        
        val productId = intent.getLongExtra("PRODUCT_ID", -1)
        if (productId != -1L) {
            loadProduct(productId)
        }
        
        findViewById<MaterialButton>(R.id.buttonEdit).setOnClickListener {
            currentProduct?.let { product ->
                val intent = Intent(this, AddEditProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                startActivity(intent)
            }
        }
        
        findViewById<MaterialButton>(R.id.buttonDelete).setOnClickListener {
            showDeleteConfirmationDialog()
        }
        
        findViewById<MaterialButton>(R.id.buttonShare).setOnClickListener {
            shareProduct()
        }
        Log.d(TAG, "✅ onCreate() - Carga inicial completada")
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "👀 onStart() - Detalle del producto visible")
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "▶️ onResume() - Actualizando información del producto")
        val productId = intent.getLongExtra("PRODUCT_ID", -1)
        if (productId != -1L) {
            loadProduct(productId)
        }
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "⏸️ onPause() - Perdiendo foco")
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "⏹️ onStop() - Ya no visible")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "💀 onDestroy() - Activity destruida")
    }
    
    private fun loadProduct(productId: Long) {
        lifecycleScope.launch {
            currentProduct = repository.getProductById(productId)
            currentProduct?.let { product ->
                findViewById<TextView>(R.id.textViewDetailName).text = product.name
                findViewById<TextView>(R.id.textViewDetailCategory).text = product.category
                findViewById<TextView>(R.id.textViewDetailPrice).text = "$${String.format("%.2f", product.price)}"
                findViewById<TextView>(R.id.textViewDetailStock).text = "Stock: ${product.stock}"
                findViewById<TextView>(R.id.textViewDetailDescription).text = product.description
            }
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.confirm_delete)
            .setPositiveButton(R.string.yes) { _, _ ->
                deleteProduct()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    
    private fun deleteProduct() {
        currentProduct?.let { product ->
            lifecycleScope.launch {
                repository.delete(product)
                Toast.makeText(this@ProductDetailActivity, R.string.product_deleted, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    
    private fun shareProduct() {
        currentProduct?.let { product ->
            // Uso de Intent implícito - Característica de Android
            val shareText = getString(
                R.string.share_text,
                product.name,
                product.description,
                product.price,
                product.stock
            )
            
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "Producto BeBeLuxury: ${product.name}")
                type = "text/plain"
            }
            
            // Mostrar chooser - Herramienta del dispositivo
            startActivity(Intent.createChooser(shareIntent, "Compartir producto mediante:"))
            Log.d(TAG, "📤 Compartiendo producto: ${product.name}")
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
