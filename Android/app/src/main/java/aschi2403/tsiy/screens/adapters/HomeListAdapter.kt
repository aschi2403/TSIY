package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.helper.DataMerger
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.fragments.HomeFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.maltaisn.icondialog.pack.IconPack
import java.text.SimpleDateFormat
import java.util.*


class HomeListAdapter(
    private var data: MutableList<IActivity>,
    val context: Context,
    private val iconPack: IconPack,
    private val toolbar: Toolbar
) :
    RecyclerView.Adapter<HomeListAdapter.DataViewHolder>() {
    val database = WorkoutRepo(context)
    private val dataMerger = DataMerger(database)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val firstLine: TextView = itemView.findViewById(R.id.firstLine)
        val secondLine: TextView = itemView.findViewById(R.id.secondLine)
        val cv: MaterialCardView = itemView.findViewById(R.id.card)
        val timeValue: TextView = itemView.findViewById(R.id.timeValue)
        val cardioPointsValue: TextView = itemView.findViewById(R.id.cardioPointsValue)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.home_cards, viewGroup, false)
        return DataViewHolder(v)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.cv.setOnClickListener {
            Navigation.findNavController(it).navigate(
                HomeFragmentDirections.actionHomeFragmentToFragmentViewFinishedActivity(
                    type = data[position] is PowerActivity,
                    id = data[position].id!!
                )
            )
        }

        val minutes = data[position].duration / 1000 / 60
        val seconds = data[position].duration / 1000 % 60

        val timeValueText = "$minutes minutes and $seconds seconds"
        holder.timeValue.text = timeValueText

        holder.cardioPointsValue.text = data[position].cardioPoints.toString()
        if (data[position] is PowerActivity) {
            val activity = data[position] as PowerActivity
            holder.firstLine.text = activity.powerActivityType.name
            holder.icon.setImageDrawable(iconPack.getIcon(activity.powerActivityType.icon)!!.drawable)
        } else {
            val activity = data[position] as GeneralActivity
            holder.firstLine.text = activity.activityType.name
            holder.icon.setImageDrawable(iconPack.getIcon(activity.activityType.icon)!!.drawable)
            val workoutName = context.getString(R.string.workout)
            if (activity.activityType.name.contains(workoutName)) {
                holder.cv.setOnClickListener {
                    (context as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    toolbar.setNavigationOnClickListener {
                        (context as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                        setData(
                            dataMerger.getData(0, workoutName)
                        )
                    }
                    setData(
                        dataMerger.getDataFromWorkoutId(0, data[position].workoutPlanId)
                    )
                }
            }
        }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            holder.icon.setColorFilter(Color.WHITE)

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
        val date = simpleDateFormat.format(Date(data[position].startDate))
        holder.secondLine.text = date

    }

    fun setData(data: MutableList<IActivity>) {
        this.data = data
        this.notifyDataSetChanged()
    }
}