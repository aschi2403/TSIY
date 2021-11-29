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
import aschi2403.tsiy.model.WorkoutEntry
import aschi2403.tsiy.model.WorkoutPlan
import aschi2403.tsiy.model.relations.IActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import aschi2403.tsiy.screens.adapters.ActivityInWorkoutAdapter

class CreateEditWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentCreateEditWorkoutBinding

    lateinit var database: WorkoutRepo

    private lateinit var adapter: ActivityInWorkoutAdapter
    private lateinit var selectedActivities: MutableList<Pair<IActivityType, WorkoutEntry>>

    private lateinit var allActivities: List<IActivityType>

    private lateinit var dialogView: DialogView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!binding.nameValue.text.isNullOrEmpty() || binding.listOfActivities.isNotEmpty()) {
                    dialogView.showYesNoDialog(
                        getString(R.string.attention),
                        getString(R.string.goBackMessage),
                        { _, _ -> findNavController().popBackStack() },
                        { _, _ -> }
                    )
                } else {
                    findNavController().popBackStack()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        dialogView = DialogView(requireContext())
        val id = requireArguments().getLong("id", -1)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_edit_workout, container, false
        )

        database = WorkoutRepo(this.requireContext())

        allActivities = database.allActivityTypes.plus(database.allPowerActivityTypes)

        binding.add.setOnClickListener {
            with(dialogView) {
                showItemCheckDialog(R.string.chooseWorkout,
                    allActivities.map { iActivityType -> iActivityType.name },
                    { _, _ ->
                        if (allActivities.isNotEmpty()) {
                            selectedActivities.add(Pair(allActivities[checkedItem], WorkoutEntry(workoutPlanId = 0, iActivityTypeId = 0, position = 0, isPowerActivity = false)))
                            adapter.notifyDataSetChanged()
                        }
                    }) { _, _ -> }
            }
        }

        binding.close.setOnClickListener { findNavController().popBackStack() }

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
                            database.insertWorkoutEntry(
                                WorkoutEntry(
                                    workoutPlanId = id,
                                    isPowerActivity = selectedActivity.first.isPowerActivity,
                                    iActivityTypeId = selectedActivity.first.id!!,
                                    position = index,
                                    repetitions = selectedActivity.second.repetitions
                                )
                            )
                        }
                    }
                } else {
                    val workoutPlan = WorkoutPlan(name = binding.nameValue.text.toString())
                    database.insertWorkoutPlan(workoutPlan)

                    selectedActivities.forEachIndexed { index, selectedActivity ->
                        database.insertWorkoutEntry(
                            WorkoutEntry(
                                workoutPlanId = workoutPlan.id!!,
                                iActivityTypeId = selectedActivity.first.id!!,
                                position = index,
                                isPowerActivity = selectedActivity.first.isPowerActivity,
                                repetitions = selectedActivity.second.repetitions
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

        val listView = binding.listOfActivities
        listView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        listView.layoutManager = llm

        selectedActivities = if (id >= 0) {
            database.workoutEntriesByWorkoutPlanId(id).sortedBy { it.position }
                .map { workoutEntry ->
                    if (workoutEntry.isPowerActivity) {
                        Pair(database.powerActivityTypeById(workoutEntry.iActivityTypeId), workoutEntry)
                    } else {
                        Pair(database.activityTypeById(workoutEntry.iActivityTypeId), workoutEntry)
                    }
                }.toMutableList()
        } else {
            mutableListOf()
        }

        adapter = ActivityInWorkoutAdapter(
            selectedActivities,
            this.requireContext(),
            IconPackProvider(this.requireContext()).loadIconPack()

        )

        listView.adapter = adapter

        return binding.root
    }

    private fun isEqual(first: MutableList<Pair<IActivityType, WorkoutEntry>>, second: List<WorkoutEntry>): Boolean {
        if (first.size != second.size) {
            return false
        }
        first.forEachIndexed { index, itemOfFirst ->
            if (itemOfFirst.first.id != second[index].iActivityTypeId) {
                return false
            }
            if(itemOfFirst.second.repetitions != second[index].repetitions) {
                return false
            }
        }
        return true
    }
}
