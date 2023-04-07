package com.example.makeupstore.repository

import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.FavouriteItem
import com.example.makeupstore.models.Product
import com.example.makeupstore.utils.Resource

interface ApiRepo {
    //API
    suspend fun getAllProducts(): Resource<Product>
    suspend fun getProductsByType(categoryName: String): Resource<Product>


    //CART
    suspend fun getAllCarts(): Resource<List<CartItem>>
    suspend fun insertNewCartItem(cartItem: CartItem): Resource<Long>
    suspend fun updateCartItem(cartItem: CartItem): Resource<Int>
    suspend fun deleteAllCartItems(): Resource<Int>
    suspend fun getCartItemCount(): Resource<Int>
    suspend fun deleteCartItem(productId: Int): Resource<Int>
    suspend fun getCartItemById(cartItemId: Int): Resource<CartItem>

    //FAV
    suspend fun getAllFavs(): Resource<List<FavouriteItem>>
    suspend fun insertNewFavItem(favItem: FavouriteItem): Resource<Long>
    suspend fun updateFavItem(favItem: FavouriteItem): Resource<Int>
    suspend fun deleteAllFavItems(): Resource<Int>
    suspend fun getFavItemCount(): Resource<Int>
    suspend fun deleteFavItem(favItemId: Int): Resource<Int>
    suspend fun getFavItemById(favItemId: Int): Resource<FavouriteItem>


}