package com.example.madlevel4task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(product: Product) {
            itemView.tvProductQuantity.text = product.quantity.toString()
            itemView.tvProductName.text = product.name
        }
    }

    /**
     * Creating the view that is going to be displayed
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    /**
     * This method will bind the correct view in te list based on the position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(products[position])
    }

    /**
     * This method will return the amount of products in the list
     */
    override fun getItemCount(): Int {
        return products.size
    }
}