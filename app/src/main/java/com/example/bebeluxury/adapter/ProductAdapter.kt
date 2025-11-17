package com.example.bebeluxury.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bebeluxury.R
import com.example.bebeluxury.model.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view, onProductClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        itemView: View,
        private val onProductClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewProductName)
        private val categoryTextView: TextView = itemView.findViewById(R.id.textViewProductCategory)
        private val priceTextView: TextView = itemView.findViewById(R.id.textViewProductPrice)
        private val stockTextView: TextView = itemView.findViewById(R.id.textViewProductStock)

        fun bind(product: Product) {
            nameTextView.text = product.name
            categoryTextView.text = product.category
            priceTextView.text = "$${String.format("%.2f", product.price)}"
            stockTextView.text = "Stock: ${product.stock}"
            
            // Aquí podrías usar Glide para cargar imágenes desde URL
            // Glide.with(itemView.context).load(product.imageUrl).into(imageView)
            
            itemView.setOnClickListener {
                onProductClick(product)
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
