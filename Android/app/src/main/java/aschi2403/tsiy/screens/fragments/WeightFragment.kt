package aschi2403.tsiy.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentWeightBinding
import aschi2403.tsiy.helper.ChartMarkerView
import aschi2403.tsiy.helper.LineChartDateFormatter
import aschi2403.tsiy.repository.WorkoutRepo
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class WeightFragment : Fragment() {
    private lateinit var database: WorkoutRepo
    private lateinit var binding: FragmentWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weight, container, false
        )

        binding.addWeight.setOnClickListener {
            addWeight()
        }

        binding.chart.xAxis.valueFormatter = LineChartDateFormatter()
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.isDoubleTapToZoomEnabled = false
        binding.chart.marker = ChartMarkerView(context, R.layout.marker_view)
        binding.chart.description.isEnabled = false

        database = WorkoutRepo(this.requireContext())

        val weightList: ArrayList<Entry> = ArrayList()
        database.allWeightEntries.forEach { weight ->
            run {
                weightList.add(
                    Entry(
                        weight.date.toFloat(),
                        weight.weight.toFloat(),
                        weight
                    )
                )
            }
        }


        val weightLine = LineDataSet(weightList, "Weight")
        weightLine.axisDependency = AxisDependency.LEFT
        weightLine.color = Color.RED
        weightLine.valueTextColor = Color.BLACK

        binding.chart.data = LineData(weightLine)
        binding.chart.invalidate() // refresh


        return binding.root
    }

    private fun addWeight() {
        findNavController().navigate(R.id.action_weightFragment_to_addWeightFragment)
    }

}
