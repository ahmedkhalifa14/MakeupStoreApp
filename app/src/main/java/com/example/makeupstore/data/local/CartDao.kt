package com.example.makeupstore.data.local

import androidx.room.*
import com.example.makeupstore.models.CartItem
import retrofit2.http.DELETE


@Dao
interface CartDao {
    @Query("select * from Cart_Table order by createdAt desc ")
    suspend fun getAllCarts(): List<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCartItem(cartItem: CartItem): Long


    @Update
    suspend fun updateCartItem(cartItem: CartItem): Int

    @Query("DELETE FROM Cart_Table")
    suspend fun deleteAllCartItems(): Int

    @Query("SELECT COUNT(id) FROM Cart_Table")
    suspend fun getCartItemCount(): Int

    @Query("select * from Cart_Table WHERE id = :id")
    suspend fun getCartItemById(id: Int): CartItem

    @Query("delete from Cart_Table where id = :id")
    suspend fun deleteCartItem(id: Int): Int

}