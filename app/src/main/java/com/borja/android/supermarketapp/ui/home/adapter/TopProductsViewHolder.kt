package com.borja.android.supermarketapp.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.borja.android.supermarketapp.databinding.ItemTopProductBinding
import com.borja.android.supermarketapp.domain.model.Product
import com.bumptech.glide.Glide

class TopProductsViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding = ItemTopProductBinding.bind(view)

    fun render(product: Product) {
        binding.apply {
            tvTitle.text = product.title
            Glide.with(tvTitle.context).load(product.imageUrl).into(ivTopProduct)
        }
    }

}