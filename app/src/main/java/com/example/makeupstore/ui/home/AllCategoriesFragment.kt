package com.example.makeupstore.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.adapters.CategoryAdapter
import com.example.makeupstore.adapters.ProductTypesAdapter
import com.example.makeupstore.databinding.FragmentAllCategoriesBinding
import com.example.makeupstore.databinding.FragmentHomeBinding
import com.example.makeupstore.utils.Constants


class AllCategoriesFragment : Fragment() {


    private var _binding: FragmentAllCategoriesBinding? = null
    private val binding get() = _binding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productTypesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllCategoriesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }
    private fun setupRecyclerView() {
        productTypesRecyclerView = binding!!.categoriesRv
        categoryAdapter = CategoryAdapter(Constants.PRODUCT_TYPE)
        productTypesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2,LinearLayoutManager.VERTICAL, false)
        productTypesRecyclerView.adapter = categoryAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}