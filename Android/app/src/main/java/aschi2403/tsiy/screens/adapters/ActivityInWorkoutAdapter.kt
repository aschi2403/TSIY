package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.relations.IActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.card.MaterialCardView
import com.maltaisn.icondialog.pack.IconPack


class ActivityInWorkoutAdapter(
    private var data: MutableList<IActivityType>,
    val context: Context,
    private val iconPack: IconPack
) :
    RecyclerView.Adapter<ActivityInWorkoutAdapter.DataViewHolder>() {

    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfActivity: TextView
        val imageOfActivity: ImageView
        val cv: MaterialCardView
        val moveUp: ImageButton
        val moveDown: ImageButton
        val delete: ImageButton

        init {
            cv = itemView.findViewById(R.id.item_card)
            nameOfActivity = itemView.findViewById(R.id.nameOfItem)
            moveUp = itemView.findViewById(R.id.moveUp)
            moveDown = itemView.findViewById(R.id.moveDown)
            delete = itemView.findViewById(R.id.deleteItem)
            imageOfActivity = itemView.findViewById(R.id.imageOfActivity)
        }
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
        holder.nameOfActivity.text = data[position].name
        holder.imageOfActivity.setImageDrawable(iconPack.getIcon(data[position].icon)?.drawable)
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
        holder.delete.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun setData(data: MutableList<IActivityType>) {
        this.data = data
    }
}