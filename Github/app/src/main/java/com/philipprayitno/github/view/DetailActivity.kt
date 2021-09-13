package com.philipprayitno.github.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.philipprayitno.github.model.User
import com.philipprayitno.github.R
import com.philipprayitno.github.view.adapter.DetailSectionsPagerAdapter
import com.philipprayitno.github.viewmodel.DetailViewModel
import com.philipprayitno.github.databinding.ActivityDetailBinding

class DetailActivity: AppCompatActivity() {

    private lateinit var user: User
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindLayout()
        setupView()
        bindViewModel()
    }

    private fun bindLayout() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)
    }

    private fun setupData() {
        supportActionBar?.title = user.userName
        binding.tvUsername.text = String.format("@%s", user.userName)
        binding.tvName.text = user.name
        binding.tvTotalFollowers.text = user.followers
        binding.tvTotalFollowing.text = user.following
        if(isFavorite) {
            binding.ibFavorite.setImageResource(R.drawable.ic_baseline_star_48_fill)
        } else {
            binding.ibFavorite.setImageResource(R.drawable.ic_baseline_star_border_48)
        }
        if(user.avatar.intAvatar != -1) {
            Glide.with(this)
                .load(user.avatar.intAvatar)
                .into(binding.imgUser)
        } else {
            Glide.with(this)
                .load(user.avatar.stringAvatar)
                .into(binding.imgUser)
        }
    }

    private fun setupView() {
        window.statusBarColor = Color.BLACK
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        binding.ibFavorite.setOnClickListener {
            if(isFavorite) {
                viewModel.removeFromFavorite(user.userName)
                Toast.makeText(this, getString(R.string.success_remove, user.userName), Toast.LENGTH_SHORT).show()
                binding.ibFavorite.setImageResource(R.drawable.ic_baseline_star_border_48)
            } else {
                viewModel.addToFavorite()
                Toast.makeText(this, getString(R.string.success_add, user.userName), Toast.LENGTH_SHORT).show()
                binding.ibFavorite.setImageResource(R.drawable.ic_baseline_star_48_fill)
            }
            isFavorite = !isFavorite
        }
    }

    private fun setupPager() {
        val sectionsPagerAdapter = DetailSectionsPagerAdapter(this, user)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERNAME, user.userName)
    }

    private fun bindViewModel() {
        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        setLoadingState(true)
        viewModel.setUser(user.userName)
        viewModel.checkIfFavorite(user.userName).observe(this, { isFavorite ->
            this.isFavorite = isFavorite
            setupData()
            setupPager()
        })
        viewModel.getUser().observe(this, { user ->
            if(user != null) {
                this.user = user
                setupData()
                setupPager()
                setLoadingState(false)
            }
        })
    }

    private fun setLoadingState(state: Boolean) {
        binding.progressBar.visibility = if(state) View.VISIBLE else { View.GONE }
        binding.rlNoData.visibility = if(state) View.VISIBLE else { View.GONE }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val USERNAME = "username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.information,
            R.string.followers,
            R.string.following
        )
    }
}