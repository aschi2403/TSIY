package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentListViewBinding
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import aschi2403.tsiy.screens.adapters.WorkoutPlanEditDeleteAdapter
import java.util.*
import java.util.stream.Collectors


class ListWorkoutPlansFragment : Fragment() {

    private lateinit var workoutPlanEditDeleteAdapter: WorkoutPlanEditDeleteAdapter
    private lateinit var database: WorkoutRepo

    private lateinit var binding: FragmentListViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list_view, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = WorkoutRepo(requireContext())

        binding.addNewActivity.setOnClickListener {
            findNavController().navigate(R.id.action_listWorkoutPlansFragment_to_createEditWorkoutFragment)
        }

        val rv = binding.rv
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        rv.layoutManager = llm
        workoutPlanEditDeleteAdapter =
            WorkoutPlanEditDeleteAdapter(
                database.allWorkoutPlans.toMutableList(),
                requireContext()
            )
        rv.adapter = workoutPlanEditDeleteAdapter

        configureSearchButton()

        return binding.root
    }

    private fun configureSearchButton() {
        binding.search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                callSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String?) {
                if (query != null) {
                    workoutPlanEditDeleteAdapter.setData(
                        database.allWorkoutPlans.stream()
                            .filter {
                                it.name.toUpperCase(Locale.getDefault()).contains(
                                    query.toUpperCase(
                                        Locale.getDefault()
                                    )
                                )
                            }
                            .collect(
                                Collectors.toList()
                            )
                    )
                    workoutPlanEditDeleteAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        workoutPlanEditDeleteAdapter.setData(database.allWorkoutPlans.toMutableList())
        workoutPlanEditDeleteAdapter.notifyDataSetChanged()
    }
}
