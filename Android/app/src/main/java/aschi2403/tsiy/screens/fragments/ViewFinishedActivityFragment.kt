package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentViewfinishedactivityBinding
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.fragment_viewfinishedactivity.view.*
import kotlinx.android.synthetic.main.table_row.view.*
import java.text.SimpleDateFormat
import java.util.*


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

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN)
        binding.startDate.text = sdf.format(iActivity.startDate)
        binding.endDate.text = sdf.format(iActivity.endDate)

        binding.duration.text =
            durationLeadingZero(iActivity.duration) + DateUtils.formatElapsedTime(iActivity.duration / 1000)

        val pauseDuration = iActivity.endDate - iActivity.startDate - iActivity.duration
        binding.pause.text = durationLeadingZero(pauseDuration) +
                DateUtils.formatElapsedTime(pauseDuration / 1000)

        binding.cardiopoints.text = iActivity.cardioPoints.toString()
        binding.caloriesValue.text = iActivity.calories.toString()

        binding.activity.text = if (powerActivity) {
            database.powerActivityTypeById(iActivity.activityTypeId).name
        } else {
            database.activityTypeById(iActivity.activityTypeId).name
        }

        if (powerActivity) {
            val sets = database.getSetEntriesByPowerActivityId(idOfActivity).toTypedArray()
            if (sets.isNotEmpty()) {
             //   binding.header.visibility = View.VISIBLE
                binding.sets.visibility = View.VISIBLE
            }
            sets.forEach {
                val row = LayoutInflater.from(context).inflate(R.layout.table_row, null)
                row.setValue.text = it.id.toString()
                row.repetitionsValue.text = it.repetitions.toString()
                row.weightValue.text = it.weight.toString()
                binding.sets.addView(row)
            }
        }

        return binding.root
    }

    private fun durationLeadingZero(duration: Long): String {
        if (duration / 1000 < 3600) {
            return "00:"
        }
        if (duration / 1000 < 36000) {
            return "0"
        }
        return ""
    }
}