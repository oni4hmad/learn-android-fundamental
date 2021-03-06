package com.dicoding.picodiploma.mysubmission2.ui.userdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission2.R
import com.dicoding.picodiploma.mysubmission2.databinding.ItemRowUserBinding
import com.dicoding.picodiploma.mysubmission2.network.UserFollowing

class ListFollowingAdapter(private val listUser: List<UserFollowing>) : RecyclerView.Adapter<ListFollowingAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val u = listUser[position]

        Glide.with(holder.binding.imgUser.context)
            .load(u.avatarUrl)
            .into(holder.binding.imgUser)
        holder.binding.tvItemUsername.text = holder.itemView.context.getString(R.string.username, u.login)

        holder.binding.imgUser.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: UserFollowing)
    }

}