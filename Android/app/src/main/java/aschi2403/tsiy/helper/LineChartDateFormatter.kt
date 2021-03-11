package aschi2403.tsiy.helper

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class LineChartDateFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat("dd.MM", Locale.GERMAN)
        return sdf.format(Date(value.toLong()))
    }
}