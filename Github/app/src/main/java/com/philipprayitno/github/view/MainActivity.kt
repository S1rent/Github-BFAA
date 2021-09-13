package com.philipprayitno.github.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.philipprayitno.github.viewmodel.MainViewModel
import com.philipprayitno.github.model.User
import com.philipprayitno.github.R
import com.philipprayitno.github.view.adapter.UserListAdapter
import com.philipprayitno.github.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object  {
        private const val STATE_RESULT = "state_result"
    }

    private lateinit var adapter: UserListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        supportActionBar?.title = getString(R.string.github_users)
        window.statusBarColor = Color.BLACK

        bindLayout()
        setupView()
        loadInstance(savedInstanceState)
        setupRecyclerView(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        setupSearchFeature(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        } else if(item.itemId == R.id.action_settings) {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    private fun setupView() {
        binding.ibFloatingFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSearchFeature(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_user)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                query = searchView.query.toString()
                setLoadingState(true)
                viewModel.setUsers(p0?.trim() ?: "", resources)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0?.isEmpty() == true) {
                    viewModel.setUsers("", resources)
                    query = ""
                    return true
                }
                return false
            }
        })
    }

    private fun setupRecyclerView(savedInstanceState: Bundle?) {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter()
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object: UserListAdapter.OnItemClickCallback {
            override fun onItemClickCallback(data: User) {
                navigateToDetail(data)
            }
        })

        if(savedInstanceState?.getString(STATE_RESULT).isNullOrBlank()) {
            viewModel.setUsers("", resources)
        }
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

    private fun navigateToDetail(data: User) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, this.query)
    }

    private fun loadInstance(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) {
            val savedQuery = savedInstanceState.getString(STATE_RESULT) ?: ""
            this.query = savedQuery
            viewModel.setUsers(savedQuery, resources)
        }
    }
}