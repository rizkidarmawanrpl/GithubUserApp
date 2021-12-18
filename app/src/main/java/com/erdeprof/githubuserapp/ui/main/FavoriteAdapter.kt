package com.erdeprof.githubuserapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdeprof.githubuserapp.R
import com.erdeprof.githubuserapp.database.User
import com.erdeprof.githubuserapp.database.Favorite
import com.erdeprof.githubuserapp.databinding.ItemFavoriteBinding
import com.erdeprof.githubuserapp.helper.FavoriteDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorites = ArrayList<Favorite>()
    fun setListFavorites(listFavorites: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(holder, listFavorites[position])
    }

    override fun getItemCount(): Int {
        return listFavorites.size
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holder: FavoriteViewHolder, favorite: Favorite) {
            with(binding) {
                val listUser = ArrayList<User>()
                val user = User(
                    favorite.avatar_url,
                    favorite.username
                )

                listUser.add(user)

                Glide.with(holder.itemView.context)
                    .load(favorite.avatar_url)
                    .placeholder(R.mipmap.ic_image_search)
                    .error(R.mipmap.ic_broken_image)
                    .into(imgItemAvatar)
                tvItemName.text = favorite.username
                tvItemFollowerValue.text = favorite.follower.toString()
                tvItemFollowingValue.text = favorite.following.toString()
                cardView.setOnClickListener {
                    val intent = Intent(it.context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USER, user)
                    intent.putExtra(DetailUserActivity.EXTRA_FAVORITE, favorite)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}