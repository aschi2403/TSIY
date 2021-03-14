package aschi2403.tsiy.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentWorkoutScreenBinding
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.fragment_workout_screen.*


class WorkoutScreenFragment : Fragment() {
    private lateinit var database: WorkoutRepo
    private var idOfActivity: Long = -1
    private var set = 0
    private lateinit var binding: FragmentWorkoutScreenBinding

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
            idOfActivity = database.addGeneralActivity(
                GeneralActivity(
                    date = System.currentTimeMillis(),
                    activityTypeId = activityTypeId
                )
            )!!
        } else {
            idOfActivity = database.addPowerActivity(
                PowerActivity(
                    date = System.currentTimeMillis(),
                    activityTypeId = activityTypeId
                )
            )!!
            binding.next.setOnClickListener {
                set++

                Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost).navigate(
                    WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                        idOfPowerActivity = idOfActivity, finished = false
                    )
                )
                // startActivityForResult(intent, 1);
            }
        }

        binding.timer.base = SystemClock.elapsedRealtime()
        binding.timer.start()
        binding.pause.setOnClickListener {
            pause()
        }
        binding.close.setOnClickListener {
            saveInDatabase(activityTypeId)
           // activity?.close
            //close
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
                    date = database.generalActivityById(idOfActivity).date,
                    time = SystemClock.elapsedRealtime() - timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0
                )
            ) //TODO: calculate cardioPoints and calories
            activity?.onBackPressed()
        } else { // power activity
            Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost).navigate(
                WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                    idOfPowerActivity = idOfActivity, finished = true
                )
            )
            database.updatePowerActivity(
                PowerActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    date = database.powerActivityById(idOfActivity).date,
                    time = SystemClock.elapsedRealtime() - timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    sets = set
                )
            )
        }
    }

    private fun pause() {
        timer.stop() //TODO: timer isn't stopping
        Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost).navigate(
            WorkoutScreenFragmentDirections.actionWorkoutScreenToPauseScreen(
            )
        )
        // startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            pause()
        } else {
            timer.start()
        }
    }
}