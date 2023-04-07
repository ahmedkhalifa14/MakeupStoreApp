package com.example.makeupstore.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Cart_Table")
data class CartItem(
    val id: Int?=0,
    val productName: String,
    val productImage: String,
    var productQuantity: Int,
    var productPrice: Double,
    var productTotalPrice:Double,
    val createdAt:Long

): Serializable{
    @PrimaryKey(autoGenerate = true)
    var productId: Int?=null
}

