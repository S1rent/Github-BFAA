package com.philipprayitno.github.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.philipprayitno.github.model.User
import com.philipprayitno.github.R
import com.philipprayitno.github.databinding.ItemGithubUserBinding

class UserListAdapter: RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClickCallback(data: User)
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var binding: ItemGithubUserBinding = ItemGithubUserBinding.bind(itemView)

        internal fun bind(user: User) {
            binding.tvName.text = if(user.name == "") "-" else { user.name }
            binding.tvUsername.text = String.format("@%s", user.userName)
            if(user.avatar.intAvatar != -1){
                Glide.with(itemView.context)
                    .load(user.avatar.intAvatar)
                    .into(binding.imgUser)
            } else {
                Glide.with(itemView.context)
                    .load(user.avatar.stringAvatar)
                    .into(binding.imgUser)
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClickCallback(user)
            }
        }
    }

    private val userList = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<User>) {
        userList.clear()
        userList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_github_user, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}