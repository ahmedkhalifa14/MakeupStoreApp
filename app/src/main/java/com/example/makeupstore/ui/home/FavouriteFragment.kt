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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.adapters.FavouriteItemsAdapter
import com.example.makeupstore.databinding.FragmentFavouriteBinding
import com.example.makeupstore.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding
    private val mainViewModel :MainViewModel by viewModels()
    private lateinit var favAdapter: FavouriteItemsAdapter
    private lateinit var favRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding!!.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObserver()
        mainViewModel.getAllFavItems(true)
        favAdapter.setOnFavItemClickListener {favItem ->
            mainViewModel.deleteFavItemByProductId(favItem.favId)
        }

    }

    private fun subscribeToObserver() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.getAllFavItemsStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                            binding!!.cardView.isVisible = false
                        },
                        onSuccess = { favItems ->
                            binding!!.spinKit.isVisible = false
                            if (favItems.isEmpty())
                            {
                                binding!!.cardView.isVisible = true
                            }
                            else {
                                favAdapter.favourites = favItems
                                Timber.tag("favList").d(favItems.toString())
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
                mainViewModel.deleteFavItemStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            mainViewModel.getAllFavItems(false)
                        },
                        onError = {}

                    )
                )
            }

        }
    }

    private fun setupRecyclerView() {
        favRecyclerView = binding!!.favRV
        favAdapter = FavouriteItemsAdapter()
        favRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        favRecyclerView.adapter = favAdapter

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}