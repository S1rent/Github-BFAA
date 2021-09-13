package com.philipprayitno.githubconsumerapp.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.philipprayitno.githubconsumerapp.viewmodel.MainViewModel
import com.philipprayitno.githubconsumerapp.view.adapter.UserListAdapter
import com.philipprayitno.githubconsumerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.GRAY))
        window.statusBarColor = Color.GRAY

        bindLayout()
        setupRecyclerView()
    }

    private fun bindLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    private fun setupRecyclerView() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter()
        binding.rvUsers.adapter = adapter

        viewModel.setUsers(this)
        setLoadingState(true)
        viewModel.getUsers().observe(this, { userList ->
            if (userList != null) {
                binding.tvNoData.visibility = if(userList.isEmpty()) View.VISIBLE else { View.GONE }
                adapter.setData(userList)
                setLoadingState(false)
            }
        })
    }

    private fun setLoadingState(state: Boolean) {
        binding.progressBar.visibility = if(state) View.VISIBLE else { View.GONE }
    }
}