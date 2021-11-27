package aschi2403.tsiy.screens.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentSettingsBinding
import aschi2403.tsiy.helper.LanguageHelper
import aschi2403.tsiy.screens.activities.MainActivity

const val DEFAULT_PAUSE_IN_SECONDS = 20L

class SettingsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        binding.manageActivities.setOnClickListener { manageActivities() }
        binding.managePowerActivities.setOnClickListener { managePowerActivities() }
        binding.manageWorkouts.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_listWorkoutPlansFragment
            )
        }
        binding.legalNotice.setOnClickListener { moveToLegalNotice() }
        sharedPreferences =
            this.requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        if (sharedPreferences.getInt("darkMode", 0) == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.enableDarkMode.isChecked = true
        }

        binding.enableDarkMode.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            if (binding.enableDarkMode.isChecked) {
                editor.putInt("darkMode", AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                editor.putInt("darkMode", AppCompatDelegate.MODE_NIGHT_NO)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            editor.apply()
        }

        binding.pauseTimeValue.setText(sharedPreferences.getLong("pauseTime", DEFAULT_PAUSE_IN_SECONDS).toString())
        binding.timeUnit.setSelection(
            if (sharedPreferences.getBoolean("timeUnitSeconds", true))
                1
            else
                0
        )

        if (sharedPreferences.getString("language", "") == "") {
            binding.english.isChecked = true
        } else {
            binding.german.isChecked = true
        }

        binding.languageGroup.setOnCheckedChangeListener { _, _ ->
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            if (binding.english.isChecked) {
                editor.remove("language")
            } else {
                editor.putString("language", "de")
            }
            LanguageHelper(this.requireContext()).changeLanguage(
                resources,
                sharedPreferences.getString("language", "")!!
            )

            this.requireActivity().recreate()
            editor.apply()
        }

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun managePowerActivities() {
        findNavController().navigate(
            SettingsFragmentDirections.actionSettingsFragmentToListActivitiesFragment(
                type = true
            )
        )

    }

    private fun manageActivities() {
        findNavController().navigate(
            SettingsFragmentDirections.actionSettingsFragmentToListActivitiesFragment(
                type = false
            )
        )
    }


    private fun moveToLegalNotice() {
        findNavController().navigate(R.id.action_settingsFragment_to_legal_NoticeFragment)
    }

    override fun onStop() {
        super.onStop()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("timeUnitSeconds", binding.timeUnit.selectedItemPosition == 1)
        editor.putLong("pauseTime", binding.pauseTimeValue.text.toString().toLong())
        editor.apply()
        editor.commit()
    }
}
