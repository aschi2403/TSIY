package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentChooseActivityTypeBinding
import aschi2403.tsiy.databinding.FragmentChoosePowerActivityTypeBinding
import aschi2403.tsiy.model.SetEntry
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.fragment_choose_power_activity_type.*

class ChoosePowerActivityTypeFragment : Fragment() {

    private lateinit var binding: FragmentChoosePowerActivityTypeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_choose_power_activity_type, container, false
        )

        val idOfPowerActivity = arguments?.getLong("idOfPowerActivity")!!

        val finished = arguments?.getBoolean("finished")!!

        if (finished) {
            binding.continueButton.text = getString(R.string.finish)
        }

        binding.continueButton.setOnClickListener {
            if (weightValue.text.isNullOrEmpty() || repetitionsValue.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please insert all data for the activity", Toast.LENGTH_LONG).show()
            } else {
                saveDataInDatabase(idOfPowerActivity)
                if (finished) {
                    activity?.finish()
                } else {
                    activity?.onBackPressed()
                }
            }
        }
        return binding.root
    }

    private fun saveDataInDatabase(idOfPowerActivity: Long) {
        val database = context?.let { WorkoutRepo(it) }!!
        database.insertSetEntry(
            SetEntry(
                weight = weightValue.text.toString().toDouble(),
                repetitions = repetitionsValue.text.toString().toInt(),
                powerActivityId = idOfPowerActivity
            )
        )
    }
}
