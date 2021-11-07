package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentChooseActivityTypeBinding
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import aschi2403.tsiy.screens.adapters.WorkoutPlanViewChooseAdapter

class ChooseWorkoutPlanFragment : Fragment() {

    private lateinit var binding: FragmentChooseActivityTypeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_activity_type, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val database = context?.let { WorkoutRepo(it) }!!

        val rv = binding.listActivitiesRV
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        rv.layoutManager = llm

        val adapter =
            WorkoutPlanViewChooseAdapter(
                database.allWorkoutPlans.toMutableList(),
                requireContext()
            )
        rv.adapter = adapter
        return binding.root
    }
}
