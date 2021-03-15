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
import aschi2403.tsiy.databinding.FragmentEditnormalactivityBinding
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo


class AddEditFragment : Fragment() {

    private lateinit var binding: FragmentEditnormalactivityBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_editnormalactivity, container, false
        )
        val newActivity = arguments?.getBoolean("new")
        val idOfActivity = arguments?.getLong("id")
        val powerActivity = arguments?.getBoolean("type")

        val database = WorkoutRepo(requireContext())
        var activity: ActivityType

        val activityName = binding.activityType
        val description = binding.descriptionValue

        val cardioPoints = binding.cardioPointsValue
        val calories = binding.caloriesValue

        binding.icon.setOnClickListener { }


        if (!newActivity!! && idOfActivity != null) {
            activity = if (powerActivity!!) {
                database.powerActivityTypeById(idOfActivity)
            } else {
                database.activityTypeById(idOfActivity)
            }

            activityName.setText(activity.name)
            // TODO: set icon
            description.setText(activity.description)
        }

        binding.close.setOnClickListener { findNavController().popBackStack() }
        binding.save.setOnClickListener {
            if (activityName.text.isNullOrEmpty() || calories.text.isNullOrEmpty() || cardioPoints.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please insert all data for the activity", Toast.LENGTH_LONG).show()
            } else {
                activity = ActivityType(
                        idOfActivity,
                        activityName.text.toString(),
                        // TODO: icon
                        "icon",
                        description.text.toString(),
                        powerActivity!!,
                        calories.text.toString().toDouble(),
                        cardioPoints.text.toString().toDouble()
                )
                if (!newActivity && idOfActivity != null) {
                    database.updateActivityType(activity)
                } else {
                    activity.id = null
                    database.addActivityType(activity)
                }
                findNavController().popBackStack()
            }
        }
        return binding.root
    }
}
