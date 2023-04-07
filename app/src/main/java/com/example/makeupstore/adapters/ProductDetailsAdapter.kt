package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.R
import com.example.makeupstore.models.ProductItem

class ProductDetailsAdapter : RecyclerView.Adapter<ProductDetailsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view)

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
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_item, parent, false))

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.itemView.apply {
            //   Glide.with(this).load(product.image).into(findViewById(R.id.chImg))
          //  findViewById<ImageView>(R.id.chImg).loadImage(product.image)
//            findViewById<TextView>(R.id.item_name).text = product.name
//            findViewById<TextView>(R.id.item_description).text = product.description
//            findViewById<TextView>(R.id.item_price).text = product.price.toString()
//            findViewById<TextView>(R.id.quantity).visibility = View.GONE
            setOnClickListener {
                onItemClickListener?.let { it(product) }
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
}