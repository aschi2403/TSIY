package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentCreateEditWorkoutBinding
import aschi2403.tsiy.model.WorkoutEntry
import aschi2403.tsiy.model.WorkoutPlan
import aschi2403.tsiy.model.relations.IActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity


class CreateEditWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentCreateEditWorkoutBinding

    lateinit var database: WorkoutRepo

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var selectedActivities: MutableList<String>

    private lateinit var allActivities: List<IActivityType>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val id = requireArguments().getLong("id", -1)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_edit_workout, container, false
        )

        database = WorkoutRepo(this.requireContext())

        allActivities = database.allActivityTypes.plus(database.allPowerActivityTypes)

        binding.add.setOnClickListener {
            showDialog(allActivities)
        }

        binding.close.setOnClickListener { findNavController().popBackStack() }

        binding.save.setOnClickListener {
            if (binding.nameValue.text!!.isNotBlank() && selectedActivities.isNotEmpty()) {
                if (id >= 0) {
                    // TODO: update
                } else {
                    val workoutPlan = WorkoutPlan(name = binding.nameValue.text.toString())
                    database.insertWorkoutPlan(workoutPlan)

                    selectedActivities.forEach { activityName ->
                        val activity = allActivities.find { a -> a.name == activityName }!!
                        database.insertWorkoutEntry(
                            WorkoutEntry(
                                workoutPlanId = workoutPlan.id!!,
                                iActivityTypeId = activity.id!!,
                                position = selectedActivities.indexOf(activityName),
                                isPowerActivity = activity.isPowerActivity
                            )
                        )
                    }
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please insert all data for the activity",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (id >= 0) {
            val workoutPlan = database.workoutPlanById(id)
            binding.nameValue.setText(workoutPlan.name)
        }

        val listView = binding.listOfWorkouts

        selectedActivities = if (id >= 0) {
            database.workoutEntriesByWorkoutPlanId(id).sortedBy { it.position }
                .map { workoutEntry ->
                    if (workoutEntry.isPowerActivity) {
                        database.powerActivityTypeById(workoutEntry.iActivityTypeId).name
                    } else {
                        database.activityTypeById(workoutEntry.iActivityTypeId).name
                    }
                }.toMutableList()
        } else {
            mutableListOf()
        }
        adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_list_item_1, selectedActivities
        )

        listView.adapter = adapter

        return binding.root
    }

    private fun showDialog(activities: List<IActivityType>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an workout")

        var checkedItem = 0 // first
        builder.setSingleChoiceItems(activities.map { iActivityType -> iActivityType.name }
            .toTypedArray(), checkedItem) { _, which ->
            checkedItem = which
            // user checked an item
        }


        builder.setPositiveButton("OK") { _, _ ->
            selectedActivities.add(activities[checkedItem].name)
            adapter.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel", null)


        val dialog = builder.create()
        dialog.show()
    }
}