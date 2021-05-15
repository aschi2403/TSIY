package aschi2403.tsiy.helper

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.WeightEntry
import aschi2403.tsiy.model.relations.IActivity
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ViewConstructor")
class ChartMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val marker: TextView = findViewById(R.id.marker)

    override fun refreshContent(e: Entry, highlight: Highlight) {
        super.refreshContent(e, highlight)
        if (e.data is WeightEntry) {
            val weightEntry = (e.data as WeightEntry)
            marker.text = String.format(
                "Weight: %s\nDate: %s",
                weightEntry.weight.toString(),
                SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN).format(weightEntry.date)
            )
        } else if (e.data is List<*>) {
            marker.text = String.format("Activities: %s", (e.data as List<*>).size)
        }
    }
}