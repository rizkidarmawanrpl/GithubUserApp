package com.erdeprof.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ListUserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_item_avatar)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvFollower: TextView = itemView.findViewById(R.id.tv_item_follower_value)
        var tvFollowing: TextView = itemView.findViewById(R.id.tv_item_following_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, follower, following, avatar) = listUser[position]
        holder.imgAvatar.setImageResource(avatar)
        holder.tvName.text = name
        holder.tvFollower.text = follower
        holder.tvFollowing.text = following
        /*holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Kamu memilih " + listUser[holder.adapterPosition].name, Toast.LENGTH_SHORT).show()
        }*/
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}