package com.borja.android.supermarketapp.data.response

import com.borja.android.supermarketapp.domain.model.Product

data class ProductResponse(
    val id:String = "",
    val image:String = "",
    val name:String = "",
    val description:String = "",
    val price:String = "",
){
    fun toDomain():Product{
        return Product(
            id = id,
            imageUrl = image,
            title = name,
            description = description,
            price = price
        )
    }
}