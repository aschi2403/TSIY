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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.helper.ButtonHelper
import aschi2403.tsiy.model.Activity
import aschi2403.tsiy.model.relations.ActivityWithCardioActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.fragments.HomeFragmentDirections
import aschi2403.tsiy.screens.fragments.ViewFinishedWorkoutFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.maltaisn.icondialog.pack.IconPack
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.concurrent.TimeUnit

class HomeListAdapter(
    private var data: List<ActivityWithCardioActivity>,
    val context: Context,
    private val iconPack: IconPack,
    private val checkedItem: Int,
    private val calledFromHomeFragment: Boolean,
    private val androidActivity: android.app.Activity
) :
    RecyclerView.Adapter<HomeListAdapter.DataViewHolder>() {
    private val repo = WorkoutRepo(context)

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
        if (calledFromHomeFragment) {
            calledFromHomeFragment(holder, position)
        } else {
            calledFromWorkoutView(position, holder)
        }

        holder.timeValue.text = generateTimeValueText(data[position].activity.duration)

        holder.cardioPointsValue.text = "0"
        holder.firstLine.text = data[position].activityType.name
        holder.icon.setImageDrawable(iconPack.getIcon(data[position].activityType.icon)!!.drawable)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            holder.icon.setColorFilter(Color.WHITE)
        }

        holder.secondLine.text = formatDate(data[position].activity.startDate)
    }

    private fun calledFromWorkoutView(
        position: Int,
        holder: DataViewHolder
    ) {
        ButtonHelper().configureDeleteButton(androidActivity) {
            repo.deleteWorkoutSessionWithActivities(data[position].workoutId!!)
            Navigation.findNavController(androidActivity, holder.cv.id).popBackStack()
        }
        holder.cv.setOnClickListener {
            val direction = ViewFinishedWorkoutFragmentDirections
            Navigation.findNavController(it).navigate(
                direction.actionViewFinishedWorkoutFragmentToFragmentViewFinishedActivity(
                    type = data[position] is Activity,
                    id = data[position].activity.id!!
                )
            )
        }
    }

    private fun calledFromHomeFragment(
        holder: DataViewHolder,
        position: Int
    ) {
        holder.cv.setOnClickListener {
            if (data[position].activity.id == null && data[position].workoutId != null) {
                (context as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                Navigation.findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToViewFinishedWorkoutFragment(
                        checkedItem,
                        data[position].workoutId!!
                    )
                )
            } else {
                Navigation.findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToFragmentViewFinishedActivity(
                        type = data[position] is Activity,
                        id = data[position].activity.id!!
                    )
                )
            }
        }
    }

    private fun formatDate(startDate: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
        return simpleDateFormat.format(Date(startDate))
    }

    private fun generateTimeValueText(
        duration: Long
    ): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        return "$minutes minutes and $seconds seconds"
    }

    fun setData(data: MutableList<ActivityWithCardioActivity>) {
        this.data = data
        this.notifyDataSetChanged()
    }
}
