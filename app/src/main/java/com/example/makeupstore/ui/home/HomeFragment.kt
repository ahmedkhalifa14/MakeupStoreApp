package com.example.makeupstore.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.R
import com.example.makeupstore.adapters.ProductAdapter
import com.example.makeupstore.adapters.ProductTypesAdapter
import com.example.makeupstore.databinding.FragmentHomeBinding
import com.example.makeupstore.models.FavouriteItem
import com.example.makeupstore.models.ProductItem
import com.example.makeupstore.models.ProductType
import com.example.makeupstore.utils.Constants.PRODUCT_TYPE
import com.example.makeupstore.utils.Constants.typesImages
import com.example.makeupstore.utils.Constants.typesNames
import com.example.makeupstore.utils.EventObserver
import com.example.makeupstore.utils.Utils
import com.example.makeupstore.utils.Utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var productTypesAdapter: ProductTypesAdapter
    private lateinit var productTypesRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRecyclerView: RecyclerView
    var type:String? = null
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var productList: ArrayList<ProductItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getAllProductData()

        subscribeToObservers()
        setupProductFilterSpinner()
        getProductFilterSpinnerSelectedItem()

        binding!!.viewAllProductsTv.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAllProductsFragment("all product")
            findNavController().navigate(action)
        }

        binding!!.viewAllCategoriesTv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allCategoriesFragment)
        }
        productTypesAdapter.setOnItemClickListener {
            val type = it.CategoryName
            val action = HomeFragmentDirections.actionHomeFragmentToAllProductsFragment(type)
            findNavController().navigate(action)
        }
        productAdapter.setOnItemClickListener {
            val productItem = it
            val action =
                HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(productItem)
            findNavController().navigate(action)
        }
        productAdapter.setOnFavItemClickListener {productItem ->
            productItem.id?.let { id ->
                productItem.name?.let { name ->
                productItem.image_link?.let { image_link ->
                    productItem.price?.let { price ->
                        FavouriteItem(id, name, image_link, price, Utils.getTimeStamp())
                    }
                }
            } }
                ?.let { favItem ->
                    mainViewModel.addNewFavItem(
                        favItem
                    )
                }

        }
    }

    private fun getProductFilterSpinnerSelectedItem() {
        binding!!.filterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    type= typesNames[position]
                    if (typesNames[position]=="all Product")
                        mainViewModel.getAllProduct()
                    else
                        mainViewModel.getProductsByType(typesNames[position])

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    type = typesNames[parent?.adapter?.getItem(0) as Int]
                    mainViewModel.getAllProduct()
                }
            }
    }

    private fun setupProductFilterSpinner() {
        val productFilterSpinnerAdapter = object : ArrayAdapter<String>
            (
            requireContext(), R.layout.product_filter_item, R.id.product_filter_tv, typesNames

        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val productFilterImage: ImageView = view.findViewById(R.id.product_filter_img)
                val image = typesImages[position]
                productFilterImage.setImageResource(image)
                return view
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val productFilterImage: ImageView = view.findViewById(R.id.product_filter_img)
                val image = typesImages[position]
                productFilterImage.setImageResource(image)
                return view
            }
        }
        binding!!.filterSpinner.adapter=productFilterSpinnerAdapter
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.productStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding!!.spinKit.isVisible = false
                            productAdapter.differ.submitList(data)
                            Log.d("here", data.toString())

                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            Log.d("here", it.toString())
                        }
                    )
                )

            }

            launch {
                mainViewModel.productByTypeStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding!!.spinKit.isVisible = false
                            productAdapter.differ.submitList(data)
                            Log.d("productByTypeStatus", data.toString())

                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            Log.d("productByTypeStatus", it.toString())
                        }
                    )
                )

            }
            launch {
                mainViewModel.addNewFavStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            if (it>0)
                                view?.showSnackBar("Item added successfully")
                        },
                        onError = {}
                    )
                )
            }
        }


    }

    private fun setupRecyclerView() {
        productTypesRecyclerView = binding!!.productTypesRv
        productTypesAdapter = ProductTypesAdapter(PRODUCT_TYPE)
        productTypesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productTypesRecyclerView.adapter = productTypesAdapter
    }

    private fun getAllProductData() {
        productRecyclerView = binding!!.prouductRv
        productList = ArrayList()
        productAdapter = ProductAdapter()
        productRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        productRecyclerView.adapter = productAdapter


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }
}