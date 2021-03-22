package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentWorkoutScreenBinding
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.viewmodel.WorkoutScreenViewModel
import kotlinx.android.synthetic.main.fragment_workout_screen.*


class WorkoutScreenFragment : Fragment() {
    private lateinit var database: WorkoutRepo
    private var idOfActivity: Long = -1
    private var set = 0
    private lateinit var binding: FragmentWorkoutScreenBinding

    private var viewModel = WorkoutScreenViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout_screen, container, false
        )

        val activityTypeId = arguments?.getLong("activityTypeId")!!

        database = WorkoutRepo(requireActivity().applicationContext)


        binding.activity.text = arguments?.getString("name")

        if (!arguments?.getBoolean("type")!!) { //normal activity
            binding.next.visibility = View.INVISIBLE
            if (idOfActivity == -1L) {
                idOfActivity =
                    database.addGeneralActivity(
                        GeneralActivity(
                            startDate = System.currentTimeMillis(),
                            activityTypeId = activityTypeId
                        )
                    )!!
            }
        } else {
            if (idOfActivity == -1L) {
                idOfActivity = database.addPowerActivity(
                    PowerActivity(
                        startDate = System.currentTimeMillis(),
                        activityTypeId = activityTypeId
                    )
                )!!
            }
            binding.next.setOnClickListener {
                set++
                Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost).navigate(
                    WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                        idOfPowerActivity = idOfActivity, finished = false
                    )
                )
            }
        }
        binding.timer.base = SystemClock.elapsedRealtime() - viewModel.values.getLong("stopTime", 0)
        binding.timer.start()

        binding.pause.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost).navigate(
                WorkoutScreenFragmentDirections.actionWorkoutScreenToPauseScreen(
                )
            )
        }
        binding.close.setOnClickListener {
            saveInDatabase(activityTypeId)
            viewModel.values.clear()
        }

        return binding.root
    }

    private fun saveInDatabase(activityTypeId: Long) {
        val type = arguments?.getBoolean("type")!!
        if (!type) { // normal activity
            database.updateActivity(
                GeneralActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    startDate = database.generalActivityById(idOfActivity).startDate,
                    duration = SystemClock.elapsedRealtime() - timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    endDate = System.currentTimeMillis()
                )
            ) //TODO: calculate cardioPoints and calories
            activity?.finish()
        } else { // power activity

            database.updatePowerActivity(
                PowerActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    startDate = database.powerActivityById(idOfActivity).startDate,
                    duration = SystemClock.elapsedRealtime() - timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    sets = set,
                    endDate = System.currentTimeMillis()
                )
            )
            Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost).navigate(
                WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                    idOfPowerActivity = idOfActivity, finished = true
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.values.putLong("stopTime", SystemClock.elapsedRealtime() - binding.timer.base)
    }
}

