package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.makeupstore.R
import com.example.makeupstore.databinding.AllCategoryItemBinding
import com.example.makeupstore.databinding.ProductTypesItemBinding
import com.example.makeupstore.models.ProductType

class CategoryAdapter(private val productTypeItem: List<ProductType>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var onItemClickListener: ((ProductType) -> Unit)? = null
    fun setOnItemClickListener(listener: (ProductType) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        return CategoryViewHolder(
            AllCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = productTypeItem[position]
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
            Glide.with(this).load(item.CategoryImage).into(holder.binding.productItemImg)
            holder.binding.categoryNameTv.text = item.CategoryName
            holder.binding.root.setBackgroundResource(item.CategoryColor)
        }

        // holder.binding.productItemImg.loadImage(item.CategoryImage.toString())



    }

    override fun getItemCount(): Int = productTypeItem.size


    class CategoryViewHolder(val binding: AllCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}