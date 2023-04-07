package com.example.makeupstore.data.local

import androidx.room.*
import com.example.makeupstore.models.FavouriteItem


@Dao
interface FavDao {
    @Query("select * from Favourite order by createdAt desc ")
    suspend fun getAllFavItems(): List<FavouriteItem>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewFavItem(favItem: FavouriteItem): Long
    @Update
    suspend fun updateFavItem(favItem: FavouriteItem): Int
    @Query("DELETE FROM Favourite")
    suspend fun deleteAllFavItems(): Int
    @Query("SELECT COUNT(favId) FROM Favourite")
    suspend fun getFavItemsCount(): Int
    @Query("select * from Favourite WHERE favId = :favId")
    suspend fun getFavItemById(favId: Int): FavouriteItem
    @Query("delete from Favourite where favId = :favId")
    suspend fun deleteFavItem(favId: Int): Int
}