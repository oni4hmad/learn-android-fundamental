package com.dicoding.picodiploma.mysubmission3.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission3.R
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUser
import com.dicoding.picodiploma.mysubmission3.databinding.ItemRowUserBinding
import com.dicoding.picodiploma.mysubmission3.helper.FavoriteUserDiffCallback

class ListFavoriteAdapter : RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder>() {

    private val listUser = ArrayList<FavoriteUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setListFavoriteUsers(listFavoriteUsers: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.listUser, listFavoriteUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(listFavoriteUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                Glide.with(imgUser.context)
                    .load(favoriteUser.avatarUrl)
                    .into(imgUser)
                tvItemUsername.text = this@ListViewHolder.itemView.context.getString(R.string.username, favoriteUser.login)

                imgUser.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this@ListViewHolder.adapterPosition]) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }

}