package com.philipprayitno.github.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.philipprayitno.github.R
import com.philipprayitno.github.databinding.ActivitySettingBinding
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.philipprayitno.github.utils.AlarmReceiver
import com.philipprayitno.github.viewmodel.SettingViewModel


class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingBinding

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var viewModel: SettingViewModel

    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        bindView()
        bindViewModel()
    }

    private fun bindViewModel() {
        this.viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SettingViewModel::class.java)

        viewModel.fetchSharedPreferenceData(this.sharedPreferences)
        viewModel.getIsChecked().observe(this, { checkedValue ->
            this.isChecked = checkedValue
            binding.swReminder.isChecked = checkedValue
            setupListener()
        })
    }

    private fun setupView() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        supportActionBar?.title = getString(R.string.github_users)
        window.statusBarColor = Color.BLACK

        alarmReceiver = AlarmReceiver()
    }

    private fun bindView() {
        this.binding = ActivitySettingBinding.inflate(layoutInflater)
        this.sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        setContentView(binding.root)
    }

    private fun setupListener() {
        binding.swReminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                alarmReceiver.setRepeatingAlarm(this,"09:00", getString(R.string.reminder_message))
            } else {
                alarmReceiver.cancelAlarm(this)
            }
            viewModel.setIsChecked(this.sharedPreferences, isChecked)

            val message = if(isChecked) getString(R.string.on_change) else { getString(R.string.off_change) }
            Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
        }
    }
}