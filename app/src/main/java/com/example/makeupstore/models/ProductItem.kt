package com.example.makeupstore.models

import java.io.Serializable

data class ProductItem(
    val api_featured_image: String?="",
    val brand: String?="",
    val category: String?="",
    val created_at: String?="",
    val currency: String?="",
    val description: String?="",
    val id: Int?=0,
    val image_link: String?="",
    val name: String?="",
    val price: String?="",
    val price_sign: String?="",
    val product_api_url: String?="",
    val product_colors: List<ProductColor>?,
    val product_link: String?="",
    val product_type: String?="",
    val rating: Double=0.0,
    val tag_list: List<String>?,
    val updated_at: String?="",
    val website_link: String?=""
) : Serializable