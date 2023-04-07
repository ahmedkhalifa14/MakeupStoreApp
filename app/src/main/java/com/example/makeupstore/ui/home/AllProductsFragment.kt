package com.example.makeupstore.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.makeupstore.adapters.Product2Adapter
import com.example.makeupstore.databinding.FragmentAllProductsBinding
import com.example.makeupstore.models.ProductItem
import com.example.makeupstore.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllProductsFragment : Fragment() {

    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var product2Adapter: Product2Adapter
    private lateinit var product2RecyclerView: RecyclerView
    private val args by navArgs<AllProductsFragmentArgs>()
    private lateinit var productList: ArrayList<ProductItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = args.type
        if (type=="all product"){
            mainViewModel.getAllProduct()
        }
        else
        mainViewModel.getProductsByType(type)
        subscribeToObservers()
        setupRecyclerView()
        product2Adapter.setOnItemClickListener {
            val product = it
            Log.d("AllProductsFragmentDirections" ,it.toString())
            val action =
                AllProductsFragmentDirections.actionAllProductsFragmentToProductDetailsFragment(
                    product
                )
            findNavController().navigate(action)
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.productByTypeStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding!!.spinKit.isVisible = false

                            product2Adapter.differ.submitList(data)
                            Log.d("allProductsFragment", data.toString())


                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            Log.d("allProductsFragment", it.toString())
                        }
                    )
                )

            }
            launch {
                mainViewModel.productStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding!!.spinKit.isVisible = false

                            product2Adapter.differ.submitList(data)
                            Log.d("allProductsFragment", data.toString())


                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            Log.d("allProductsFragment", it.toString())
                        }
                    )
                )

            }


        }

    }

    private fun setupRecyclerView() {
        product2RecyclerView = binding!!.allProductsFragmentRv
        productList = ArrayList()
        product2Adapter = Product2Adapter()
        product2RecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        product2RecyclerView.adapter = product2Adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}