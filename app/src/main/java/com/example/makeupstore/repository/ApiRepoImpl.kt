package com.example.makeupstore.repository

import com.example.makeupstore.data.local.CartDao
import com.example.makeupstore.data.local.FavDao
import com.example.makeupstore.data.network.MakeupApiService
import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.FavouriteItem
import com.example.makeupstore.models.Product
import com.example.makeupstore.qualifiers.IOThread
import com.example.makeupstore.utils.Resource
import com.example.makeupstore.utils.Utils.safeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepoImpl @Inject constructor(
    private val makeupApiService: MakeupApiService,
    @IOThread
    private val dispatcher: CoroutineDispatcher,
    private val cartDao: CartDao,
    private val favDao: FavDao
) : ApiRepo {

    override suspend fun getAllProducts(): Resource<Product> =
        withContext(dispatcher) {
            safeCall {
                val result = makeupApiService.getAllProducts()
                Resource.Success(result)
            }
        }

    override suspend fun getProductsByType(categoryName: String): Resource<Product> =
        withContext(dispatcher) {
            safeCall {
                val result = makeupApiService.getProductsByType(categoryName)
                Resource.Success(result)
            }
        }

    /*******************************Room******************************/
    //CART
    override suspend fun getAllCarts(): Resource<List<CartItem>> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.getAllCarts()
                Resource.Success(result)
            }
        }

    override suspend fun insertNewCartItem(cartItem: CartItem): Resource<Long> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.insertNewCartItem(cartItem)
                Resource.Success(result)
            }
        }

    override suspend fun updateCartItem(cartItem: CartItem): Resource<Int> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.updateCartItem(cartItem)
                Resource.Success(result)
            }
        }

    override suspend fun deleteAllCartItems(): Resource<Int> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.deleteAllCartItems()
                Resource.Success(result)
            }
        }

    override suspend fun getCartItemCount(): Resource<Int> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.getCartItemCount()
                Resource.Success(result)
            }
        }

    override suspend fun deleteCartItem(productId: Int): Resource<Int> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.deleteCartItem(productId)
                Resource.Success(result)
            }
        }

    override suspend fun getCartItemById(cartItemId: Int): Resource<CartItem> =
        withContext(dispatcher) {
            safeCall {
                val result = cartDao.getCartItemById(cartItemId)
                Resource.Success(result)
            }
        }
    /****************************ROOM*********************************/
    //FAV
    override suspend fun getAllFavs(): Resource<List<FavouriteItem>> =
        withContext(dispatcher){
            safeCall {
                val result =favDao.getAllFavItems()
                Resource.Success(result)
            }
        }


    override suspend fun insertNewFavItem(favItem: FavouriteItem): Resource<Long> =
        withContext(dispatcher){
            safeCall {
                val result = favDao.insertNewFavItem(favItem)
                Resource.Success(result)
            }
        }
    override suspend fun updateFavItem(favItem: FavouriteItem): Resource<Int> =
        withContext(dispatcher){
            safeCall {
                val result= favDao.updateFavItem(favItem)
                Resource.Success(result)
            }
        }

    override suspend fun deleteAllFavItems(): Resource<Int> =
        withContext(dispatcher){
            safeCall {
                val result =favDao.deleteAllFavItems()
                Resource.Success(result)
            }
        }

    override suspend fun getFavItemCount(): Resource<Int> =
        withContext(dispatcher){
            safeCall {
                val result =favDao.getFavItemsCount()
                Resource.Success(result)
            }
        }

    override suspend fun deleteFavItem(favItemId: Int): Resource<Int> =
        withContext(dispatcher){
            safeCall {
                val result =favDao.deleteFavItem(favItemId)
                Resource.Success(result)
            }
        }

    override suspend fun getFavItemById(favItemId: Int): Resource<FavouriteItem> =
        withContext(dispatcher){
            safeCall {
                val result = favDao.getFavItemById(favItemId)
                Resource.Success(result)
            }
        }


}