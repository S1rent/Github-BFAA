package com.philipprayitno.github.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.philipprayitno.github.model.UserResponseWrapper
import com.philipprayitno.github.R
import com.philipprayitno.github.databinding.ItemGithubUserSimpleBinding

class DetailFollowingListAdapter: RecyclerView.Adapter<DetailFollowingListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemGithubUserSimpleBinding.bind(itemView)

        internal fun bind(following: UserResponseWrapper) {
            binding.tvUsername.text = String.format("@%s", following.username)
            Glide.with(itemView.context)
                    .load(following.avatar)
                    .into(binding.imgUser)
        }
    }

    private val followingList = ArrayList<UserResponseWrapper>()

    fun setData(items: ArrayList<UserResponseWrapper>) {
        followingList.clear()
        followingList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_github_user_simple, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val following: UserResponseWrapper = followingList[position]
        holder.bind(following)
    }

    override fun getItemCount(): Int {
        return followingList.size
    }
}