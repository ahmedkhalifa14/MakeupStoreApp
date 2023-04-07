package com.example.makeupstore.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.R
import com.example.makeupstore.adapters.CommentAdapter
import com.example.makeupstore.adapters.ViewPagerAdapter
import com.example.makeupstore.databinding.FragmentProductDetailsBinding
import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.Comment
import com.example.makeupstore.models.ProductItem
import com.example.makeupstore.utils.EventObserver
import com.example.makeupstore.utils.Utils
import com.example.makeupstore.utils.Utils.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var productImageList: ArrayList<String>
    private val mainViewModel: MainViewModel by viewModels()
    private val firebaseViewModel: FirebaseViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentRecyclerView: RecyclerView

    @Inject
    lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(requireContext())
        val product = args.product
        displayProductData(product)
        subscribeToObserver(product)
        product.id?.let { firebaseViewModel.getComments(it) }
        binding!!.fragmentDetailViewPager.adapter = viewPagerAdapter
        binding!!.fragmentDetailComment.setOnClickListener {
            bottomSheet(product)
        }
        binding!!.fragmentCartAddToCartBtn.setOnClickListener {
            val currentProduct = product.price?.let { price ->
                product.name?.let { name ->
                    product.image_link?.let { image_link ->
                        CartItem(
                            product.id, name, image_link, 1,
                            price.toDouble(),
                            price.toDouble(), Utils.getTimeStamp()
                        )
                    }
                }
            }
            if (currentProduct != null) {
                mainViewModel.addNewProductToCart(currentProduct)
            }

        }
        setupCommentRv()
    }

    private fun setupCommentRv() {
        commentRecyclerView = binding!!.fragmentDetailCommentRv
        commentAdapter = CommentAdapter()
        commentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        commentRecyclerView.adapter = commentAdapter
    }

    private fun subscribeToObserver(product: ProductItem) {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.addNewProductStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            if (it > 0)
                                view?.showSnackBar("Item added successfully")
                        },
                        onError = {}
                    )
                )
            }
            launch {
                firebaseViewModel.getCommentStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            commentAdapter.commentList = it

                        },
                        onError = {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                )
            }
            launch {
                firebaseViewModel.sendCommentStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            product.id?.let { id
                                ->
                                firebaseViewModel.getComments(id)
                            }
                        },
                        onError = {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                )
            }
        }
    }

    private fun displayProductData(product: ProductItem) {
        binding!!.fragmentDetailProgressBar.visibility = View.GONE
        binding!!.fragmentDetailTitle.text = product.name
        binding!!.fragmentDetailTotalPrice.text = product.price + " $"
        binding!!.fragmentDetailFeatures.text = product.description
        binding!!.fragmentDetailCategory.text = "category: " + product.category
        binding!!.fragmentDetailBrand.text = "brand: " + product.brand

        productImageList = ArrayList()
        product.image_link?.let { productImageList.add(it) }
        viewPagerAdapter?.updateList(productImageList)

    }

    private fun bottomSheet(product: ProductItem) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.comment_bottom_sheet)
        val sendButton = bottomSheetDialog.findViewById<Button>(R.id.bottom_sheet_send)
        val comment = bottomSheetDialog.findViewById<EditText>(R.id.bottom_sheet_comment)

        sendButton?.setOnClickListener {
            if (comment != null && comment.text.toString().isNotEmpty()) {
                auth.currentUser?.displayName?.let { userName ->
                    product.id?.let { id ->
                        firebaseViewModel.sendComment(
                            userName, comment.text.toString(),
                            id
                        )
                    }
                    Toast.makeText(
                        requireContext(),
                        auth.currentUser!!.displayName,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.show()
    }

}