package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.WorkoutPlan
import aschi2403.tsiy.screens.activities.MainWorkoutActivity
import com.google.android.material.card.MaterialCardView


class WorkoutPlanViewChooseAdapter(
    private var data: MutableList<WorkoutPlan>,
    private val context: Context
) :
    RecyclerView.Adapter<WorkoutPlanViewChooseAdapter.DataViewHolder>() {

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView
        val name: TextView
        val cv: MaterialCardView

        init {
            cv = itemView.findViewById(R.id.item_card)
            name = itemView.findViewById(R.id.nameOfItem)
            icon = itemView.findViewById(R.id.imageOfActivity)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_of_items, viewGroup, false)
        return DataViewHolder(
            v
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.cv.setOnClickListener { view: View ->
            val intent = Intent(view.context, MainWorkoutActivity::class.java)
            intent.putExtra("workoutId", data[position].id!!.toInt())
            context.startActivity(intent)
        }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            holder.icon.setColorFilter(Color.WHITE)

    }

    fun setData(data: MutableList<WorkoutPlan>) {
        this.data = data
    }
}