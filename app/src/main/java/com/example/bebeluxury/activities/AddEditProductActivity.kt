package com.example.bebeluxury.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bebeluxury.R
import com.example.bebeluxury.data.AppDatabase
import com.example.bebeluxury.model.Product
import com.example.bebeluxury.repository.ProductRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddEditProductActivity : AppCompatActivity() {
    
    private lateinit var repository: ProductRepository
    private var productId: Long = -1
    private var isEditMode = false
    
    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextDescription: TextInputEditText
    private lateinit var editTextPrice: TextInputEditText
    private lateinit var autoCompleteCategory: AutoCompleteTextView
    private lateinit var editTextStock: TextInputEditText
    private lateinit var editTextImageUrl: TextInputEditText
    private lateinit var buttonSave: MaterialButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)
        
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val database = AppDatabase.getDatabase(this)
        repository = ProductRepository(database.productDao())
        
        // Inicializar vistas
        editTextName = findViewById(R.id.editTextProductName)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextPrice = findViewById(R.id.editTextPrice)
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory)
        editTextStock = findViewById(R.id.editTextStock)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        buttonSave = findViewById(R.id.buttonSave)
        
        // Configurar dropdown de categorías
        val categories = resources.getStringArray(R.array.product_categories)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        autoCompleteCategory.setAdapter(adapter)
        
        // Verificar si es modo edición
        productId = intent.getLongExtra("PRODUCT_ID", -1)
        if (productId != -1L) {
            isEditMode = true
            toolbar.title = getString(R.string.edit)
            loadProduct(productId)
        }
        
        buttonSave.setOnClickListener {
            saveProduct()
        }
    }
    
    private fun loadProduct(productId: Long) {
        lifecycleScope.launch {
            val product = repository.getProductById(productId)
            product?.let {
                editTextName.setText(it.name)
                editTextDescription.setText(it.description)
                editTextPrice.setText(it.price.toString())
                autoCompleteCategory.setText(it.category, false)
                editTextStock.setText(it.stock.toString())
                editTextImageUrl.setText(it.imageUrl)
            }
        }
    }
    
    private fun saveProduct() {
        val name = editTextName.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val priceStr = editTextPrice.text.toString().trim()
        val category = autoCompleteCategory.text.toString().trim()
        val stockStr = editTextStock.text.toString().trim()
        val imageUrl = editTextImageUrl.text.toString().trim()
        
        // Validar campos
        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || 
            category.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return
        }
        
        val price = priceStr.toDoubleOrNull()
        val stock = stockStr.toIntOrNull()
        
        if (price == null || stock == null) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return
        }
        
        lifecycleScope.launch {
            if (isEditMode) {
                // Actualizar producto existente
                val product = Product(
                    id = productId,
                    name = name,
                    description = description,
                    price = price,
                    category = category,
                    stock = stock,
                    imageUrl = imageUrl
                )
                repository.update(product)
                Toast.makeText(this@AddEditProductActivity, R.string.product_updated, Toast.LENGTH_SHORT).show()
            } else {
                // Crear nuevo producto
                val product = Product(
                    name = name,
                    description = description,
                    price = price,
                    category = category,
                    stock = stock,
                    imageUrl = imageUrl
                )
                repository.insert(product)
                Toast.makeText(this@AddEditProductActivity, R.string.product_added, Toast.LENGTH_SHORT).show()
            }
            finish()
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
