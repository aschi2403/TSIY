package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.WorkoutEntry
import aschi2403.tsiy.model.relations.IActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.maltaisn.icondialog.pack.IconPack


class ActivityInWorkoutAdapter(
    private var data: MutableList<Pair<IActivityType, WorkoutEntry>>,
    val context: Context,
    private val iconPack: IconPack
) :
    RecyclerView.Adapter<ActivityInWorkoutAdapter.DataViewHolder>() {

    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfActivity: TextView = itemView.findViewById(R.id.nameOfItem)
        val imageOfActivity: ImageView = itemView.findViewById(R.id.imageOfActivity)
        val moveUp: ImageButton = itemView.findViewById(R.id.moveUp)
        val moveDown: ImageButton = itemView.findViewById(R.id.moveDown)
        val delete: ImageButton = itemView.findViewById(R.id.deleteItem)
        val repetitionsOfWorkoutEntry: TextView = itemView.findViewById(R.id.number_of_repetitions)
        val increase: ImageButton = itemView.findViewById(R.id.increase_number_of_repetitions)
        val decrease: ImageButton = itemView.findViewById(R.id.decrease_number_of_repetitions)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_move_delete_item, viewGroup, false)
        return DataViewHolder(v)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.nameOfActivity.text = data[position].first.name

        holder.repetitionsOfWorkoutEntry.text = data[position].second.repetitions.toString()

        holder.imageOfActivity.setImageDrawable(iconPack.getIcon(data[position].first.icon)?.drawable)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            holder.imageOfActivity.setColorFilter(Color.WHITE)

        if (position == 0) {
            holder.moveUp.visibility = View.GONE
        } else {
            holder.moveUp.visibility = View.VISIBLE
            holder.moveUp.setOnClickListener {
                data.add(position - 1, data.removeAt(position))
                notifyDataSetChanged()
            }
        }
        if (data.size == position + 1) {
            holder.moveDown.visibility = View.GONE
        } else {
            holder.moveDown.visibility = View.VISIBLE
            holder.moveDown.setOnClickListener {
                data.add(position + 1, data.removeAt(position))
                notifyDataSetChanged()

            }
        }

        holder.increase.setOnClickListener {
            data[position].second.repetitions++
            notifyDataSetChanged()
        }

        holder.decrease.setOnClickListener {
            if (data[position].second.repetitions >= 2) {
                data[position].second.repetitions--
                notifyDataSetChanged()
            }
        }

        holder.delete.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }
}