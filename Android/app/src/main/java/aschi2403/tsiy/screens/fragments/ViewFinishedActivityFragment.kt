package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentViewfinishedactivityBinding
import aschi2403.tsiy.databinding.FragmentWeightBinding
import aschi2403.tsiy.repository.WorkoutRepo

class ViewFinishedActivityFragment : Fragment() {

    private lateinit var binding: FragmentViewfinishedactivityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_viewfinishedactivity, container, false
        )

        val idOfActivity = arguments?.getLong("id")
        val powerActivity = arguments?.getBoolean("type")

        val database = activity?.let { WorkoutRepo(it) }!!
        val iActivity = if (powerActivity!!) {
            database.powerActivityById(idOfActivity!!)
        } else {
            database.generalActivityById(idOfActivity!!)
        }

        binding.date.text = iActivity.date.toString()


        binding.activity.text = if (powerActivity) {
            database.powerActivityTypeById(iActivity.activityTypeId).name
        } else {
            database.activityTypeById(iActivity.activityTypeId).name
        }

        database.activityTypeById(idOfActivity)

        return binding.root
    }
}