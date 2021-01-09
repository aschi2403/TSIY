package aschi2403.tsiy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeListAdapter(private var data: List<IActivity>?, context: Context) :
    RecyclerView.Adapter<HomeListAdapter.DataViewHolder>() {
    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon //TODO: implement
                : ImageView
        val firstLine: TextView
        val secondLine: TextView
        val cv: MaterialCardView
        val timeValue: TextView
        val cardioPointsValue: TextView

        init {
            cv = itemView.findViewById(R.id.card)
            firstLine = itemView.findViewById(R.id.firstLine)
            secondLine = itemView.findViewById(R.id.secondLine)
            icon = itemView.findViewById(R.id.icon)
            timeValue = itemView.findViewById(R.id.timeValue)
            cardioPointsValue = itemView.findViewById(R.id.cardioPointsValue)
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.home_cards, viewGroup, false)
        return DataViewHolder(v)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        if (data != null) {
            //holder.name.text = data!![position].
            holder.cv.setOnClickListener {
                // TODO: implement edit, view activity
            }

            val minutes = data!![position].time / 1000 / 60
            val seconds = data!![position].time / 1000 % 60

            holder.timeValue.text = "$minutes minutes and $seconds seconds"
            holder.cardioPointsValue.text = data!![position].cardioPoints.toString()
            val name: String
            name = if (data!![position] is PowerActivity) {
                val activity = data!![position] as PowerActivity
                activity.powerActivityType.name
            } else {
                val activity = data!![position] as GeneralActivity
                activity.activityType.name
            }
            holder.firstLine.text = name

            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            val date = simpleDateFormat.format(Date(data!![position].date))
            holder.secondLine.text = date
        }
    }

    fun setData(data: List<IActivity>?) {
        this.data = data
    }

}