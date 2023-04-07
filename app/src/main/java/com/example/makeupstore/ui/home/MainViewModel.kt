package com.example.makeupstore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.makeupstore.data.local.DataStoreManager
import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.FavouriteItem
import com.example.makeupstore.models.Product
import com.example.makeupstore.models.User
import com.example.makeupstore.qualifiers.MainThread
import com.example.makeupstore.repository.ApiRepoImpl
import com.example.makeupstore.utils.Event
import com.example.makeupstore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ApiRepoImpl,
    @MainThread
    private val dispatcher: CoroutineDispatcher,
    private val dataStoreManager: DataStoreManager

) : ViewModel() {

    /******************API****************************************/
    private val _productStatus =
        MutableStateFlow<Event<Resource<Product>>>(Event(Resource.Init()))
    val productStatus: MutableStateFlow<Event<Resource<Product>>> =
        _productStatus

    private val _productByTypeStatus =
        MutableStateFlow<Event<Resource<Product>>>(Event(Resource.Init()))
    val productByTypeStatus: MutableStateFlow<Event<Resource<Product>>> =
        _productByTypeStatus

    /*************************Room********************************/
    private val _addNewProductStatus =
        MutableStateFlow<Event<Resource<Long>>>(Event(Resource.Init()))
    val addNewProductStatus: MutableStateFlow<Event<Resource<Long>>> =
        _addNewProductStatus
    private val _getCartItemStatus =
        MutableStateFlow<Event<Resource<CartItem>>>(Event(Resource.Init()))
    val getCartItemStatus: MutableStateFlow<Event<Resource<CartItem>>> =
        _getCartItemStatus
    private val _getAllCartItemStatus =
        MutableStateFlow<Event<Resource<List<CartItem>>>>(Event(Resource.Init()))
    val getAllCartItemsStatus: MutableStateFlow<Event<Resource<List<CartItem>>>> =
        _getAllCartItemStatus
    private val _deleteCartItemStatus =
        MutableStateFlow<Event<Resource<Int>>>(Event(Resource.Init()))
    val deleteCartItemStatus: MutableStateFlow<Event<Resource<Int>>> =
        _deleteCartItemStatus
    private val _updateCartItemStatus =
        MutableStateFlow<Event<Resource<Int>>>(Event(Resource.Init()))
    val updateCartItemStatus: MutableStateFlow<Event<Resource<Int>>> =
        _updateCartItemStatus
    private val _getCartItemCountStatus =
        MutableStateFlow<Event<Resource<Int>>>(Event(Resource.Init()))
    val getCartItemCountStatus: MutableStateFlow<Event<Resource<Int>>> =
        _getCartItemCountStatus
/*******************************************************************************/
private val _addNewFavStatus =
    MutableStateFlow<Event<Resource<Long>>>(Event(Resource.Init()))
    val addNewFavStatus: MutableStateFlow<Event<Resource<Long>>> =
        _addNewFavStatus
    private val _getFavItemStatus =
        MutableStateFlow<Event<Resource<FavouriteItem>>>(Event(Resource.Init()))
    val getFavItemStatus: MutableStateFlow<Event<Resource<FavouriteItem>>> =
        _getFavItemStatus
    private val _getAllFavItemStatus =
        MutableStateFlow<Event<Resource<List<FavouriteItem>>>>(Event(Resource.Init()))
    val getAllFavItemsStatus: MutableStateFlow<Event<Resource<List<FavouriteItem>>>> =
        _getAllFavItemStatus
    private val _deleteFavItemStatus =
        MutableStateFlow<Event<Resource<Int>>>(Event(Resource.Init()))
    val deleteFavItemStatus: MutableStateFlow<Event<Resource<Int>>> =
        _deleteFavItemStatus
    private val _updateFavItemStatus =
        MutableStateFlow<Event<Resource<Int>>>(Event(Resource.Init()))
    val updateFavItemStatus: MutableStateFlow<Event<Resource<Int>>> =
        _updateFavItemStatus
    private val _getFavItemCountStatus =
        MutableStateFlow<Event<Resource<Int>>>(Event(Resource.Init()))
    val getFavItemCountStatus: MutableStateFlow<Event<Resource<Int>>> =
        _getFavItemCountStatus

    /******************************API*****************************************/
    fun getAllProduct() {
        viewModelScope.launch(dispatcher) {
            _productStatus.emit(Event(Resource.Loading()))
            val result = repository.getAllProducts()
            _productStatus.emit(Event(result))
        }
    }

    fun getProductsByType(category: String) {
        viewModelScope.launch(dispatcher) {
            _productByTypeStatus.emit(Event(Resource.Loading()))
            val result = repository.getProductsByType(category)
            Timber.tag("viewModel").d(result.data.toString())
            _productByTypeStatus.emit(Event(result))
        }
    }

    /*************************ROOM**********************************************/
    fun addNewProductToCart(cartItem: CartItem) {
        viewModelScope.launch(dispatcher) {
            _addNewProductStatus.emit(Event(Resource.Loading()))
            val result = repository.insertNewCartItem(cartItem)
            _addNewProductStatus.emit(Event(result))
        }

    }

    fun getCartItemById(cartItemId: Int) {
        viewModelScope.launch(dispatcher) {
            _getCartItemStatus.emit(Event(Resource.Loading()))
            val result = repository.getCartItemById(cartItemId)
            _getCartItemStatus.emit(Event(result))
        }
    }

    fun getAllCartItems(loading: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (loading) _getAllCartItemStatus.emit(Event(Resource.Loading()))
            val result = repository.getAllCarts()
            _getAllCartItemStatus.emit(Event(result))
        }
    }

    fun deleteCartItemByProductId(cartItemId: Int) {
        viewModelScope.launch(dispatcher) {
            _deleteCartItemStatus.emit(Event(Resource.Loading()))
            val result = repository.deleteCartItem(cartItemId)
            _deleteCartItemStatus.emit(Event(result))
        }
    }

    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch(dispatcher) {
            _updateCartItemStatus.emit(Event(Resource.Loading()))
            val result = repository.updateCartItem(cartItem)
            _updateCartItemStatus.emit(Event(result))

        }
    }

    fun getCartItemCount() {
        viewModelScope.launch(dispatcher) {
            _getCartItemCountStatus.emit(Event(Resource.Loading()))
            val result = repository.getCartItemCount()
            _getCartItemCountStatus.emit(Event(result))
        }
    }

    /****************************************************************/
    fun addNewFavItem(favouriteItem: FavouriteItem) {
        viewModelScope.launch(dispatcher) {
            _addNewFavStatus.emit(Event(Resource.Loading()))
            val result = repository.insertNewFavItem(favouriteItem)
            _addNewFavStatus.emit(Event(result))
        }

    }

    fun getFavItemById(favItemId: Int) {
        viewModelScope.launch(dispatcher) {
            _getFavItemStatus.emit(Event(Resource.Loading()))
            val result = repository.getFavItemById(favItemId)
            _getFavItemStatus.emit(Event(result))
        }
    }

    fun getAllFavItems(loading: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (loading) _getAllFavItemStatus.emit(Event(Resource.Loading()))
            val result = repository.getAllFavs()
            _getAllFavItemStatus.emit(Event(result))
        }
    }

    fun deleteFavItemByProductId(favItemId: Int) {
        viewModelScope.launch(dispatcher) {
            _deleteFavItemStatus.emit(Event(Resource.Loading()))
            val result = repository.deleteFavItem(favItemId)
            _deleteFavItemStatus.emit(Event(result))
        }
    }

    fun updateFavItem(favouriteItem: FavouriteItem) {
        viewModelScope.launch(dispatcher) {
            _updateFavItemStatus.emit(Event(Resource.Loading()))
            val result = repository.updateFavItem(favouriteItem)
            _updateFavItemStatus.emit(Event(result))

        }
    }

    fun getFavItemCount() {
        viewModelScope.launch(dispatcher) {
            _getFavItemCountStatus.emit(Event(Resource.Loading()))
            val result = repository.getFavItemCount()
            _getFavItemCountStatus.emit(Event(result))
        }
    }

    /********************************Datastore*********************************/

    val isFirstTimeLaunch = dataStoreManager.isFirstTimeLaunch().asLiveData(dispatcher)
    val getUserInfo = dataStoreManager.getUserInfo().asLiveData(dispatcher)
    fun setFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        GlobalScope.launch(dispatcher) {
            dataStoreManager.setFirstTimeLaunch(isFirstTimeLaunch)

        }
    }

    fun setUserINfo(userInfo: User) {
        GlobalScope.launch(dispatcher) {
            dataStoreManager.setUserInfo(userInfo)
        }
    }

}