package com.philipprayitno.github.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.philipprayitno.github.view.adapter.DetailFollowerListAdapter
import com.philipprayitno.github.viewmodel.DetailViewModel
import com.philipprayitno.github.databinding.FragmentDetailFollowerBinding

class DetailFollowerFragment : Fragment() {

    private lateinit var binding: FragmentDetailFollowerBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var adapter: DetailFollowerListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        loadInstance(savedInstanceState)
        setupRecyclerView()
    }

    private fun loadInstance(savedInstanceState: Bundle?) {
        var savedUsername = savedInstanceState?.getString(USERNAME) ?: ""

        if(savedInstanceState == null) {
            savedUsername = arguments?.getString(USERNAME, "").toString()
        }

        setLoadingState(true)
        viewModel.setUserFollowerList(savedUsername)
        viewModel.getUserFollowerList().observe(viewLifecycleOwner, { data ->
            if(data != null) {
                adapter.setData(data)
                setLoadingState(false)
                binding.rlNoData.visibility = if(data.size == 0) View.VISIBLE else { View.GONE }
                binding.tvNoData.visibility = if(data.size == 0) View.VISIBLE else { View.GONE }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.layoutManager = LinearLayoutManager(context)
        adapter = DetailFollowerListAdapter()
        binding.rvFollowers.adapter = adapter
    }

    private fun setLoadingState(state: Boolean) {
        binding.progressBar.visibility = if(state) View.VISIBLE else { View.GONE }
    }

    companion object {
        const val  USERNAME = "username"
    }
}