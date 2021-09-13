package com.philipprayitno.github.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philipprayitno.github.databinding.FragmentDetailInfoBinding

class DetailInfoFragment: Fragment() {

    private lateinit var binding: FragmentDetailInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    private fun setupData() {
        binding.tvCompany.text = arguments?.getString(USER_COMPANY, "-")
        binding.tvLocation.text = arguments?.getString(USER_LOCATION, "-")
        binding.tvRepository.text = arguments?.getString(USER_REPOSITORY, "0")
    }

    companion object {
        const val USER_COMPANY = "user_company"
        const val USER_LOCATION = "user_location"
        const val USER_REPOSITORY = "user_repository"
    }
}