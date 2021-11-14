package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentChoosePowerActivityTypeBinding
import aschi2403.tsiy.model.SetEntry
import aschi2403.tsiy.repository.WorkoutRepo

class ChoosePowerActivityTypeFragment : Fragment() {

    private lateinit var binding: FragmentChoosePowerActivityTypeBinding

    lateinit var database: WorkoutRepo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_choose_power_activity_type, container, false
        )


        database = context?.let { WorkoutRepo(it) }!!

        val idOfPowerActivity = arguments?.getLong("idOfPowerActivity")!!

        val upNext = arguments?.getString("upNext")


        val finished = arguments?.getBoolean("finished")!!

        if (finished) {
            binding.continueButton.text = getString(R.string.finish)
        }

        binding.continueButton.setOnClickListener {
            if (binding.weightValue.text.isNullOrEmpty() || binding.repetitionsValue.text.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Please insert all data for the activity",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                saveDataInDatabase(idOfPowerActivity)
                if (finished) {
                    activity?.finish()
                } else {
                    findNavController().navigate(ChoosePowerActivityTypeFragmentDirections.actionChoosePowerActivityTypeToPauseScreen(upNext = upNext))
                }
            }
        }

        val sets = database.getSetEntriesByPowerActivityId(idOfPowerActivity)
        if (sets.isNotEmpty()) {
            binding.repetitionsValue.setText(sets.last().repetitions.toString())
            binding.weightValue.setText(sets.last().weight.toString())
        }

        binding.addWeight.setOnClickListener {
            binding.weightValue.setText(
                preventNegativeValues(toDoubleOrZero(binding.weightValue.text.toString()) + 1.0)
                    .toString()
            )
        }
        binding.removeWeight.setOnClickListener {
            binding.weightValue.setText(
                preventNegativeValues(toDoubleOrZero(binding.weightValue.text.toString()) - 1.0).toString()
            )
        }
        binding.addRepetition.setOnClickListener {
            binding.repetitionsValue.setText(
                preventNegativeValues(toIntOrZero(binding.repetitionsValue.text.toString()) + 1).toString()
            )
        }
        binding.removeRepetition.setOnClickListener {
            binding.repetitionsValue.setText(
                preventNegativeValues(toIntOrZero(binding.repetitionsValue.text.toString()) - 1).toString()
            )
        }
        return binding.root
    }

    private fun preventNegativeValues(number: Number): Any {
        if (number.toDouble() < 0) {
            if (number is Double) {
                return 0.0
            }
            return 0
        }
        return number
    }

    private fun saveDataInDatabase(idOfPowerActivity: Long) {
        database.insertSetEntry(
            SetEntry(
                weight = binding.weightValue.text.toString().toDouble(),
                repetitions = binding.repetitionsValue.text.toString().toInt(),
                powerActivityId = idOfPowerActivity
            )
        )
    }

    private fun toDoubleOrZero(textToConvert: String): Double {
        return textToConvert.toDoubleOrNull() ?: return 0.0
    }

    private fun toIntOrZero(textToConvert: String): Int {
        return textToConvert.toIntOrNull() ?: return 0
    }
}
