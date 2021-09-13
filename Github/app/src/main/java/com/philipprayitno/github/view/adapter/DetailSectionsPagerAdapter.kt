package com.philipprayitno.github.view.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.philipprayitno.github.model.User
import com.philipprayitno.github.view.DetailFollowerFragment
import com.philipprayitno.github.view.DetailFollowingFragment
import com.philipprayitno.github.view.DetailInfoFragment

class DetailSectionsPagerAdapter(activity: AppCompatActivity, private val user: User): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> {
                fragment = DetailInfoFragment()
                fragment.apply {
                    arguments = Bundle().apply {
                        putString(DetailInfoFragment.USER_COMPANY, user.company)
                        putString(DetailInfoFragment.USER_LOCATION, user.location)
                        putString(DetailInfoFragment.USER_REPOSITORY, user.repository)
                    }
                }
            }
            1 -> {
                fragment = DetailFollowerFragment()
                fragment.apply {
                    arguments = Bundle().apply {
                        putString(DetailFollowerFragment.USERNAME, user.userName)
                    }
                }
            }
            2 -> {
                fragment = DetailFollowingFragment()
                fragment.apply {
                    arguments = Bundle().apply {
                        putString(DetailFollowerFragment.USERNAME, user.userName)
                    }
                }
            }
        }
        return fragment as Fragment
    }
}