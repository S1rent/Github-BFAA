package com.philipprayitno.github.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.philipprayitno.github.model.UserResponseWrapper
import com.philipprayitno.github.R
import com.philipprayitno.github.databinding.ItemGithubUserSimpleBinding

class DetailFollowerListAdapter: RecyclerView.Adapter<DetailFollowerListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemGithubUserSimpleBinding.bind(itemView)

        internal fun bind(follower: UserResponseWrapper) {
            binding.tvUsername.text = String.format("@%s", follower.username)
            Glide.with(itemView.context)
                .load(follower.avatar)
                .into(binding.imgUser)
        }
    }

    private val followerList = ArrayList<UserResponseWrapper>()

    fun setData(items: ArrayList<UserResponseWrapper>) {
        followerList.clear()
        followerList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_github_user_simple, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val follower: UserResponseWrapper = followerList[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int {
        return followerList.size
    }
}