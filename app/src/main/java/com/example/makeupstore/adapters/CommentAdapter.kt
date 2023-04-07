package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.databinding.CommentItemBinding
import com.example.makeupstore.models.CartItem
import com.example.makeupstore.models.Comment


class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(private val itemBinding: CommentItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(currentItem: Comment) {
            itemBinding.root.apply {
                itemBinding.commentUserNameTV.text = currentItem.userName
                itemBinding.commentTextTV.text=currentItem.comment
                itemBinding.commentTimeTV.text=currentItem.date.toDate().toString()

            }


        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemBinding =
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CommentViewHolder(itemBinding)
    }
     var commentList: List<Comment>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    private val diffCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }


        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)
    override fun getItemCount(): Int = commentList.size

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        val currentItem = commentList[position]
        holder.apply {
            bindData(currentItem)
        }
    }


    fun updateList(newList: List<Comment>) {
       // list.clear()
      //  list.addAll(newList)
        notifyDataSetChanged()
    }
}







