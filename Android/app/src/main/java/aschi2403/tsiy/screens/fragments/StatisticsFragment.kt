package aschi2403.tsiy.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentStatisticsBinding
import aschi2403.tsiy.helper.ChartMarkerView
import aschi2403.tsiy.helper.LineChartDateFormatter
import aschi2403.tsiy.repository.WorkoutRepo
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*


class StatisticsFragment : Fragment() {

    private lateinit var formatter: SimpleDateFormat
    private lateinit var database: WorkoutRepo
    private lateinit var binding: FragmentStatisticsBinding
    private var workoutList: MutableList<Entry> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false
        )

        database = WorkoutRepo(this.requireContext())
        changeChart(getString(R.string.Day))

        binding.workoutsChart.xAxis.valueFormatter = LineChartDateFormatter()
        binding.workoutsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.workoutsChart.isDoubleTapToZoomEnabled = false
        binding.workoutsChart.marker = ChartMarkerView(context, R.layout.marker_view)
        binding.workoutsChart.description.isEnabled = false

        formatter = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN)

        // daily cardio points and calories

        val activities = database.allGeneralActivities.filter { generalActivity ->
            formatter.parse(formatter.format(Date(generalActivity.startDate))) == formatter.parse(
                formatter.format(Date())
            )
        }.plus(database.allPowerActivities.filter { powerActivity ->
            formatter.parse(formatter.format(Date(powerActivity.startDate))) == formatter.parse(
                formatter.format(Date())
            )
        })
        binding.caloriesValue.text = activities.sumByDouble { a -> a.calories }.toString()
        binding.cardioPointsValue.text = activities.sumByDouble { a -> a.cardioPoints }.toString()

        val items = arrayOf(
            getString(R.string.Day),
            getString(R.string.Week),
            getString(R.string.Month),
            getString(R.string.Year)
        )

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_spinner_item, items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.groupBy.adapter = adapter
        binding.groupBy.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // noting to do
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                binding.groupByLabel.text = p0?.selectedItem.toString()
                changeChart(p0?.selectedItem.toString())
                changeCardioPoints(position)
            }
        }

        return binding.root
    }

    fun changeCardioPoints(position: Int) {
        //TODO: implement
    }

    fun changeChart(groupBy: String) {
        workoutList.clear()
        when (groupBy) {
            getString(R.string.Day) -> groupBy(
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )
            )
            getString(R.string.Week)
            -> groupBy(
                SimpleDateFormat(
                    "F/MM/yyyy",
                    Locale.getDefault()
                )
            )
            getString(R.string.Month) -> groupBy(
                SimpleDateFormat(
                    "MM/yyyy",
                    Locale.getDefault()
                )
            )
            getString(R.string.Year) -> groupBy(
                SimpleDateFormat(
                    "yyyy",
                    Locale.getDefault()
                )
            )
        }
        val weightLine =
            LineDataSet(workoutList, "${getString(R.string.activities)} / $groupBy")
        weightLine.color = Color.RED
        weightLine.valueTextColor = Color.BLACK


        binding.workoutsChart.data = LineData(weightLine)
        binding.workoutsChart.notifyDataSetChanged()
        binding.workoutsChart.invalidate()
    }

    private fun groupBy(groupByFormatter: SimpleDateFormat) {
        database.allPowerActivities.plus(database.allGeneralActivities)
            .groupBy {
                groupByFormatter.parse(
                    groupByFormatter.format(Date(it.startDate))
                )
            }.entries.forEach { list ->
                workoutList.add(
                    Entry(list.key!!.time.toFloat(), list.value.size.toFloat(), list.value)
                )
            }

    }
}
