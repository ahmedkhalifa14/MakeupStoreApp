package com.example.makeupstore.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Favourite")
data class FavouriteItem (
    val favId:Int,
    val favName:String,
    val favImage:String,
    val favPrice:String,
    val createdAt:Long
){
    @PrimaryKey(autoGenerate = true)
    var productId: Int?=null
}