package com.dicoding.picodiploma.mysubmission2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission2.databinding.ItemRowUserBinding

class ListFollowerAdapter(private val listUser: List<UserFollower>) : RecyclerView.Adapter<ListFollowerAdapter.ListViewHolder>() {

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

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)


    interface OnItemClickCallback {
        fun onItemClicked(data: UserFollower)
    }

}