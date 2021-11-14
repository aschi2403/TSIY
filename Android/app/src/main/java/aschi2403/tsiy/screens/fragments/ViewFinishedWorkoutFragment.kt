package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentHomeBinding
import aschi2403.tsiy.helper.DataMerger
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.adapters.HomeListAdapter

class ViewFinishedWorkoutFragment : Fragment() {

    private lateinit var database: WorkoutRepo
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        val checkedItem = arguments?.getInt("checkedItem")!!
        val rv = binding.listOfActivities
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(this.context)

        database = WorkoutRepo(this.requireContext())

        val dataMerger = DataMerger(database)

        rv.layoutManager = llm
        val homeListAdapter = HomeListAdapter(
            dataMerger.getDataFromWorkoutId(
                checkedItem,
                arguments?.getInt("workoutId")!!
            ),
            requireContext(),
            IconPackProvider(requireContext()).loadIconPack(),
            checkedItem,
            false
        )

        hideStartActivityButton()

        rv.adapter = homeListAdapter

        return binding.root
    }

    private fun hideStartActivityButton() {
        binding.startGeneralActivity.visibility = View.INVISIBLE
        binding.startPowerActivity.visibility = View.INVISIBLE
        binding.startWorkout.visibility = View.INVISIBLE
        binding.startNewActivity.visibility = View.INVISIBLE
    }
}
