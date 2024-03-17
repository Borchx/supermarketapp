package com.borja.android.supermarketapp.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.borja.android.supermarketapp.databinding.ItemProductBinding
import com.borja.android.supermarketapp.domain.model.Product
import com.bumptech.glide.Glide

class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemProductBinding.bind(view)

    fun render(product: Product){
        binding.apply {
            Glide.with(binding.tvTitle.context).load(product.imageUrl).into(ivProduct)
            tvTitle.text = product.title
            tvDescription.text = product.description
            tvPrice.text = "${product.price} €"
        }
    }
}