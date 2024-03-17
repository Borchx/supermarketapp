package com.borja.android.supermarketapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.borja.android.supermarketapp.R
import com.borja.android.supermarketapp.domain.model.Product

class ProductsAdapter(private var products:List<Product> = emptyList()):RecyclerView.Adapter<ProductsViewHolder>() {

    fun updateList(products:List<Product>){
        this.products = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.render(products[position])
    }
}