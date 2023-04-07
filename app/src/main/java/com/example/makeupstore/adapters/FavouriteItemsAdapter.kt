package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.makeupstore.R
import com.example.makeupstore.databinding.ProductItemBinding
import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.FavouriteItem
import kotlin.coroutines.coroutineContext

class FavouriteItemsAdapter () :
    RecyclerView.Adapter<FavouriteItemsAdapter.FavouriteViewHolder>() {
     var favourites: List<FavouriteItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<FavouriteItem>() {
        override fun areContentsTheSame(oldItem: FavouriteItem, newItem: FavouriteItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: FavouriteItem, newItem: FavouriteItem): Boolean {
            return oldItem.favId == newItem.favId
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    inner class FavouriteViewHolder(private val itemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
            fun bindData(favItem:FavouriteItem){
                itemBinding.root.apply {
                    Glide.with(context).load(favItem.favImage).into(itemBinding.productImg)
                    itemBinding.productNameTv.text=favItem.favName
                    itemBinding.productPriceTv.text=favItem.favName
                    itemBinding.favIcon.setImageResource(R.drawable.fav)
                    itemBinding.favIcon.setOnClickListener {
                        onFavIconItemClickListener?.let { item ->
                            item(favItem)
                        }
                    }
                }

            }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val itemBinding =
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = favourites.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favItem = favourites[position]
        holder.apply {
            bindData(favItem)
        }
    }


    private var onFavIconItemClickListener: ((FavouriteItem) -> Unit)? = null
    fun setOnFavItemClickListener(listener: (FavouriteItem) -> Unit) {
        onFavIconItemClickListener = listener
    }

}