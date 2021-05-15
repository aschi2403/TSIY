package aschi2403.tsiy.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentStatisticsBinding
import aschi2403.tsiy.helper.ChartMarkerView
import aschi2403.tsiy.helper.LineChartDateFormatter
import aschi2403.tsiy.repository.WorkoutRepo
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatisticsFragment : Fragment() {

    private lateinit var database: WorkoutRepo
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false
        )

        database = WorkoutRepo(this.requireContext())

        binding.workoutsChart.xAxis.valueFormatter = LineChartDateFormatter()
        binding.workoutsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.workoutsChart.isDoubleTapToZoomEnabled = false
        binding.workoutsChart.marker = ChartMarkerView(context, R.layout.marker_view)
        binding.workoutsChart.description.isEnabled = false

        val workoutList: ArrayList<Entry> = ArrayList()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN)

        //TODO: let user change groupBy to show count of activities per day,month, week or year
        database.allPowerActivities.plus(database.allGeneralActivities)
            .groupBy { formatter.parse(formatter.format(Date(it.startDate))) }.entries.forEach { list ->
                workoutList.add(
                    Entry(list.key!!.time.toFloat(), list.value.size.toFloat(), list.value)
                )
            }

        //TODO: extract string resource
        val weightLine = LineDataSet(workoutList, "Activities/Day")
        weightLine.axisDependency = YAxis.AxisDependency.LEFT
        weightLine.color = Color.RED
        weightLine.valueTextColor = Color.BLACK

        binding.workoutsChart.data = LineData(weightLine)
        binding.workoutsChart.invalidate()

        return binding.root
    }
}