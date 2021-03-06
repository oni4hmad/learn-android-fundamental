package com.dicoding.picodiploma.myrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.myrecyclerview.databinding.ItemRowHeroBinding

class ListHeroAdapter(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    /* membuat ViewHolder baru yang berisi layout item yang digunakan */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListHeroAdapter.ListViewHolder {
        /*val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)*/ // ganti view binding
        val binding = ItemRowHeroBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    /* menetapkan data yang ada ke dalam ViewHolder sesuai dengan posisinya dengan menggunakan listHero[position]
    *   binding: Proses yang mengatribusikan tampilan (ViewHolder) ke datanya */
    override fun onBindViewHolder(holder: ListHeroAdapter.ListViewHolder, position: Int) {
        val (name, description, photo) = listHero[position]
        /*holder.imgPhoto.setImageResource(photo)*/
        Glide.with(holder.itemView.context)
            .load(photo)                        // URL Gambar
            .circleCrop()                       // Mengubah image menjadi lingkaran
            .into(holder.binding.imgItemPhoto)  // imageView mana yang akan diterapkan
        holder.binding.tvItemName.text = name
        holder.binding.tvItemDescription.text = description

        /*holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Kamu memilih " + listHero[holder.adapterPosition].name, Toast.LENGTH_SHORT).show()
        }*/

        /* membuat event listener untuk setiap ViewHolder */
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listHero[holder.adapterPosition]) }
    }

    /* menetapkan ukuran dari list data yang ingin ditampilkan.  */
    override fun getItemCount(): Int = listHero.size

    /* ViewHolder dalam RecyclerView: adalah wrapper seperti View yang berisi layout untuk setiap item dalam daftar RecyclerView.
    *   tempat untuk menginisialisasi setiap komponen pada layout item dengan menggunakan itemView.findViewById.
    *   Adapter akan membuat objek ViewHolder seperlunya, serta menetapkan data untuk tampilan tersebut.
    *   Proses yang mengatribusikan tampilan ke datanya disebut binding.  */
    class ListViewHolder(var binding: ItemRowHeroBinding) : RecyclerView.ViewHolder(binding.root) {
        /*var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)*/ // ganti view binding

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Hero)
    }
}