package com.example.makeupstore.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.adapters.CartAdapter
import com.example.makeupstore.databinding.FragmentCartBinding
import com.example.makeupstore.utils.EventObserver
import com.example.makeupstore.utils.Utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        subscribeToObserver()
        cartAdapter.setOnItemCounterClickListener { cartItem, i ->
            mainViewModel.updateCartItem(cartItem)
        }
        mainViewModel.getAllCartItems(true)
        cartAdapter.setOnItemClickListener {
            mainViewModel.deleteCartItemByProductId(it.id!!)
        }
    }

    private fun subscribeToObserver() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.getAllCartItemsStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                            binding!!.cardView.isVisible = false
                        },
                        onSuccess = { cartItems ->
                            binding!!.spinKit.isVisible = false
                            if (cartItems.isEmpty())
                                binding!!.cardView.isVisible = true
                            else {
                                cartAdapter.cartList = cartItems.toMutableList()
                                Timber.tag("cartList").d(cartItems.toString())
                            }
                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            binding!!.noItemsTv.text = it.toString()
                            binding!!.cardView.isVisible = true
                        }
                    )

                )
            }
            launch {
                mainViewModel.updateCartItemStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {

                            mainViewModel.getAllCartItems(false)
                        },
                        onError = {
                            view?.showSnackBar(it)
                        }
                    )
                )
            }
            launch {
                mainViewModel.deleteCartItemStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            mainViewModel.getAllCartItems(false)
                        },
                        onError = {}
                    )
                )
            }
        }
    }

    private fun setupRecyclerView() {
        cartRecyclerView = binding!!.cartRV
        cartAdapter = CartAdapter()
        cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartRecyclerView.adapter = cartAdapter

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}