package com.philipprayitno.github.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.philipprayitno.github.R
import com.philipprayitno.github.databinding.ActivityFavoriteBinding
import com.philipprayitno.github.model.User
import com.philipprayitno.github.view.adapter.UserListAdapter
import com.philipprayitno.github.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var adapter: UserListAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindLayout()
        setupView()
    }

    private fun bindLayout() {
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FavoriteViewModel::class.java)
    }

    private fun setupView() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        supportActionBar?.title = getString(R.string.favorite)
        window.statusBarColor = Color.BLACK

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter()
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object: UserListAdapter.OnItemClickCallback {
            override fun onItemClickCallback(data: User) {
                navigateToDetail(data)
            }
        })

        viewModel.setUsers()
        setLoadingState(true)
        viewModel.getUsers().observe(this, { userList ->
            setLoadingState(false)
            if (userList != null) {
                binding.tvNoData.visibility = if(userList.isEmpty()) View.VISIBLE else { View.GONE }
                adapter.setData(userList)
            }
        })
    }

    private fun setLoadingState(state: Boolean) {
        binding.progressBar.visibility = if(state) View.VISIBLE else { View.GONE }
    }

    private fun navigateToDetail(data: User) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intent)
    }
}