package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
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
    private val iconPack: IconPack
) :
    RecyclerView.Adapter<HomeListAdapter.DataViewHolder>() {
    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView
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

        holder.timeValue.text = "$minutes minutes and $seconds seconds"
        holder.cardioPointsValue.text = data[position].cardioPoints.toString()
        if (data[position] is PowerActivity) {
            val activity = data[position] as PowerActivity
            holder.firstLine.text = activity.powerActivityType.name
            holder.icon.setImageDrawable(iconPack.getIcon(activity.powerActivityType.icon)!!.drawable)
        } else {
            val activity = data[position] as GeneralActivity
            holder.firstLine.text = activity.activityType.name
            holder.icon.setImageDrawable(iconPack.getIcon(activity.activityType.icon)!!.drawable)
        }

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val date = simpleDateFormat.format(Date(data[position].startDate))
        holder.secondLine.text = date

    }

    fun setData(data: MutableList<IActivity>) {
        this.data = data
    }

}