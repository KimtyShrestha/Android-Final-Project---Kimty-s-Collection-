package com.example.a35b_crud.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a35b_crud.R
import com.example.a35b_crud.model.Product

class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.textViewProductName)
        private val tvDescription: TextView = itemView.findViewById(R.id.textViewProductDescription)
        private val tvPrice: TextView = itemView.findViewById(R.id.textViewProductPrice)

        fun bind(product: Product) {
            tvName.text = product.name
            tvDescription.text = product.description
            tvPrice.text = "$${product.price}"
        }
    }
}
