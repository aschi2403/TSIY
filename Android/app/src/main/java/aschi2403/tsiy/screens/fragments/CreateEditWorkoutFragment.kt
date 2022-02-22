package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentCreateEditWorkoutBinding
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.WorkoutPlanEntry
import aschi2403.tsiy.model.WorkoutPlan
import aschi2403.tsiy.model.relations.IActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import aschi2403.tsiy.screens.adapters.ActivityInWorkoutAdapter


class CreateEditWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentCreateEditWorkoutBinding

    lateinit var database: WorkoutRepo

    private lateinit var adapter: ActivityInWorkoutAdapter
    private lateinit var selectedActivities: MutableList<IActivityType>

    private lateinit var allActivities: List<IActivityType>

    private lateinit var dialogView: DialogView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!binding.nameValue.text.isNullOrEmpty() || binding.listOfActivities.isNotEmpty()) {
                    dialogView.showYesNoDialog(
                        getString(R.string.attention),
                        getString(R.string.goBackMessage)
                    ) { _, _ -> findNavController().popBackStack() }
                } else {
                    findNavController().popBackStack()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        dialogView = DialogView(requireContext())
        val id = requireArguments().getLong("id", -1)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_edit_workout, container, false
        )

        database = WorkoutRepo(this.requireContext())

        allActivities = database.allActivityTypes

        configureAddButton()

        binding.close.setOnClickListener { findNavController().popBackStack() }

        configureSaveButton(id)

        if (id >= 0) {
            val workoutPlan = database.workoutPlanById(id)
            binding.nameValue.setText(workoutPlan.name)
        }

        val listView = binding.listOfActivities
        listView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        listView.layoutManager = llm

        selectedActivities = getSelectedActivities(id)
        adapter = ActivityInWorkoutAdapter(
            selectedActivities,
            this.requireContext(),
            IconPackProvider(this.requireContext()).loadIconPack()
        )

        listView.adapter = adapter

        return binding.root
    }

    private fun getSelectedActivities(id: Long): MutableList<IActivityType> =
        if (id >= 0) {
            database.workoutEntriesByWorkoutPlanId(id).sortedBy { it.position }
                .map { workoutEntry ->
                    database.allActivityTypeById(workoutEntry.iActivityTypeId)
                }.toMutableList()
        } else {
            mutableListOf()
        }

    private fun configureSaveButton(id: Long) {
        binding.save.setOnClickListener {
            if (binding.nameValue.text!!.isNotBlank() && selectedActivities.isNotEmpty()) {
                if (id >= 0) {
                    val workoutEntries =
                        database.allWorkoutEntry.filter { a -> a.workoutPlanId == id }
                    if (!isEqual(selectedActivities, workoutEntries)) {
                        workoutEntries.forEach {
                            database.deleteWorkoutEntry(it)
                        }
                        selectedActivities.forEachIndexed { index, selectedActivity ->
                            insertNewWorkoutEntry(
                                id,
                                selectedActivity.id!!,
                                index
                            )
                        }
                    }
                } else {
                    val workoutPlan = WorkoutPlan(name = binding.nameValue.text.toString())
                    database.insertWorkoutPlan(workoutPlan)

                    selectedActivities.forEachIndexed { index, selectedActivity ->
                        insertNewWorkoutEntry(
                            workoutPlan.id!!,
                            selectedActivity.id!!,
                            index
                        )
                    }
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.pleaseInsertAllDataForTheActivity,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun insertNewWorkoutEntry(
        id: Long,
        activityTypeId: Long,
        index: Int,
    ) {
        database.insertWorkoutEntry(
            WorkoutPlanEntry(
                workoutPlanId = id,
                iActivityTypeId = activityTypeId,
                position = index
            )
        )
    }

    private fun configureAddButton() {
        binding.add.setOnClickListener {
            with(dialogView) {
                showItemCheckDialog(R.string.chooseWorkout,
                    allActivities.map { iActivityType -> iActivityType.name },
                    { _, _ ->
                        if (allActivities.isNotEmpty()) {
                            selectedActivities.add(allActivities[checkedItem])
                            adapter.notifyDataSetChanged()
                        }
                    }) { _, _ -> }
            }
        }
    }

    @Suppress("ReturnCount")
    private fun isEqual(first: MutableList<IActivityType>, second: List<WorkoutPlanEntry>): Boolean {
        if (first.size != second.size) {
            return false
        }
        first.forEachIndexed { index, itemOfFirst ->
            if (itemOfFirst.id != second[index].iActivityTypeId) {
                return false
            }
        }
        return true
    }
}
