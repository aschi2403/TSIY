package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.fragments.ListActivitiesFragmentDirections
import com.google.android.material.card.MaterialCardView


class ActivitiesEditDeleteAdapter(
    private var data: MutableList<ActivityType>,
    var context: Context
) :
    RecyclerView.Adapter<ActivitiesEditDeleteAdapter.DataViewHolder>() {
    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon //TODO: implement
                : ImageView
        val name: TextView
        val edit: ImageButton
        val delete: ImageButton
        val cv: MaterialCardView

        init {
            cv = itemView.findViewById(R.id.iactivity_card)
            name = itemView.findViewById(R.id.nameOfActivity)
            edit = itemView.findViewById(R.id.editActivity)
            delete = itemView.findViewById(R.id.deleteActivity)
            icon = itemView.findViewById(R.id.imageOfActivity)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_iactivity, viewGroup, false)
        return DataViewHolder(
            v
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.delete.setOnClickListener {

            val items = if (data[position].powerActivity) {
                database.allPowerActivities.stream()
                    .filter { activity: PowerActivity -> activity.activityTypeId == data[position].id }.count()
            } else {
                database.allGeneralActivities.stream()
                    .filter { activity: GeneralActivity -> activity.activityTypeId == data[position].id }.count()
            }
            if (items > 0) {
                showAlertDialog(items, position)
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
                type = data[position].powerActivity
            )
        )
    }

    fun setData(data: List<ActivityType>?) {
        this.data = data!!.toMutableList()
    }

    private fun showAlertDialog(items: Long, position: Int) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle(context.getString(R.string.attention))
        alertDialog.setMessage("Do you really want to delete $items saved activities?")
        alertDialog.setPositiveButton(
            "YES"
        ) { _, _ ->
            deleteData(position)
            Toast.makeText(context, "Activity type and saved activities deleted.", Toast.LENGTH_LONG).show()
        }
        alertDialog.setNegativeButton(
            "NO"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.show()
    }

    private fun deleteData(position: Int) {
        database.deleteActivityType(data[position])
        data.removeAt(position)
        notifyDataSetChanged()
    }

}