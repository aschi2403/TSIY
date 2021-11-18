package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.fragments.ListActivitiesFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.maltaisn.icondialog.pack.IconPack


class ActivitiesTypeEditDeleteAdapter(
    private var data: MutableList<ActivityType>,
    var context: Context,
    private val iconPack: IconPack
) :
    RecyclerView.Adapter<ActivitiesTypeEditDeleteAdapter.DataViewHolder>() {
    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.imageOfActivity)
        val name: TextView = itemView.findViewById(R.id.nameOfItem)
        val edit: ImageButton = itemView.findViewById(R.id.editItem)
        val delete: ImageButton = itemView.findViewById(R.id.deleteItem)
        val cv: MaterialCardView = itemView.findViewById(R.id.item_card)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_edit_delete_item, viewGroup, false)
        return DataViewHolder(
            v
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.icon.setImageDrawable(iconPack.getIcon(data[position].icon)!!.drawable)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            holder.icon.setColorFilter(Color.WHITE)
        holder.delete.setOnClickListener {
            val items = if (data[position].isPowerActivity) {
                database.allPowerActivities.stream()
                    .filter { activity: PowerActivity -> activity.activityTypeId == data[position].id }
                    .count()
            } else {
                database.allGeneralActivities.stream()
                    .filter { activity: GeneralActivity -> activity.activityTypeId == data[position].id }
                    .count()
            }
            if (items > 0) {
                DialogView(context).showYesNoDialog(
                    context.getString(R.string.attention),
                    "Do you really want to delete $items saved activities?",
                    { _, _ ->
                        deleteData(position)
                        Toast.makeText(
                            context,
                            "Activity type and saved activities deleted.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    { _, _ -> })
            } else {
                deleteData(position)
            }
        }

        holder.cv.setOnClickListener {
            editActivityType(it, position)
        }

        holder.edit.setOnClickListener {
            editActivityType(it, position)
        }
    }

    private fun editActivityType(view: View, position: Int) {
        Navigation.findNavController(view).navigate(
            ListActivitiesFragmentDirections.actionListActivitiesFragmentToFragmentAddEditFragment(
                id = data[position].id!!,
                type = data[position].isPowerActivity
            )
        )
    }

    fun setData(data: MutableList<ActivityType>) {
        this.data = data
    }


    private fun deleteData(position: Int) {
        database.deleteActivityType(data[position])
        data.removeAt(position)
        notifyDataSetChanged()
    }

}
