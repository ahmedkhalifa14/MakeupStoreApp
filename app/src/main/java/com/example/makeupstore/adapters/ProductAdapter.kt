package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.makeupstore.R
import com.example.makeupstore.databinding.FragmentFavouriteBinding
import com.example.makeupstore.databinding.ProductItemBinding
import com.example.makeupstore.models.Product
import com.example.makeupstore.models.ProductItem

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    private val differCallBack = object : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context) , parent,
                false)

        )

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productItem = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(productItem.image_link).placeholder(R.drawable.foundation)
                .into(findViewById(R.id.product_img))
            findViewById<TextView>(R.id.product_name_tv).text = productItem.name
            findViewById<TextView>(R.id.product_price_tv).text =
                productItem.price + " $"
            setOnClickListener {
                onItemClickListener?.let { it(productItem) }
            }
            findViewById<ImageView>(R.id.favIcon).setOnClickListener {
                onFavItemClickListener?.let { it(productItem) }
            }
            productItem.rating.apply {
                if(this>0&&this<=1){
                    holder.binding.star1.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                }
                if (this>1&&this<=2){
                    holder.binding.star1.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star2.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                }
                if (this>2&&this<=3){

                    holder.binding.star1.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star2.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star3.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                }
                if (this>3&&this<=4){
                    holder.binding.star1.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star2.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star3.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star4.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                }
                if (this>4&&this<=5){
                    holder.binding.star1.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star2.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star3.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star4.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
                    holder.binding.star5.setColorFilter(
                        ContextCompat.getColor(
                            holder.binding.star1.context,
                            R.color.starsColor
                        )
                    )
            }

            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ProductItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ProductItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onFavItemClickListener: ((ProductItem) -> Unit)? = null
    fun setOnFavItemClickListener(listener: (ProductItem) -> Unit) {
        onFavItemClickListener = listener
    }
}