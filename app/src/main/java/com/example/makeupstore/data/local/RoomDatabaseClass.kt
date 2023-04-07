package com.example.makeupstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.FavouriteItem


@Database(entities = [CartItem::class,FavouriteItem::class], version = 1, exportSchema = false)

abstract class RoomDatabaseClass:RoomDatabase() {
    abstract fun cartDao():CartDao
    abstract fun favDao():FavDao

}