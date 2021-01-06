package aschi2403.tsiy.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )
        binding.manageactivities.setOnClickListener { manageActivities() }
        binding.managepoweractivities.setOnClickListener { managePowerActivities() }
        binding.lifecycleOwner = this


        return binding.root
    }

    private fun managePowerActivities() {
        val i = Intent(requireView().context, ListActivities::class.java)
        i.putExtra("type", true)
        startActivity(i)

    }

    private fun manageActivities() {
        val i = Intent(requireView().context, ListActivities::class.java)
        i.putExtra("type", false)
        startActivity(i)
    }

}