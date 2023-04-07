package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.makeupstore.R
import com.example.makeupstore.databinding.ProductTypesItemBinding
import com.example.makeupstore.models.ProductType
import com.example.makeupstore.utils.loadImage

class ProductTypesAdapter(private val productTypeItem: List<ProductType>) :
    RecyclerView.Adapter<ProductTypesAdapter.ProductTypeViewHolder>() {
    private var onItemClickListener: ((ProductType) -> Unit)? = null
    fun setOnItemClickListener(listener: (ProductType) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductTypeViewHolder {
        return ProductTypeViewHolder(
            ProductTypesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }

    override fun onBindViewHolder(holder: ProductTypeViewHolder, position: Int) {
        val item = productTypeItem[position]
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
            Glide.with(this).load(item.CategoryImage).into(findViewById(R.id.product_item_img))
        }

       // holder.binding.productItemImg.loadImage(item.CategoryImage.toString())



    }

    override fun getItemCount(): Int = productTypeItem.size


    class ProductTypeViewHolder(val binding: ProductTypesItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}