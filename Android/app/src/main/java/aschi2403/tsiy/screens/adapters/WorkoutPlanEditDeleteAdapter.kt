package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.WorkoutPlan
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.fragments.ListWorkoutPlansFragmentDirections
import com.google.android.material.card.MaterialCardView


class WorkoutPlanEditDeleteAdapter(
    private var data: MutableList<WorkoutPlan>,
    val context: Context
) :
    RecyclerView.Adapter<WorkoutPlanEditDeleteAdapter.DataViewHolder>() {
    val database = WorkoutRepo(context)

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfWorkout: TextView = itemView.findViewById(R.id.nameOfItem)
        val cv: MaterialCardView = itemView.findViewById(R.id.item_card)
        val edit: ImageButton = itemView.findViewById(R.id.editItem)
        val delete: ImageButton = itemView.findViewById(R.id.deleteItem)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_edit_delete_item, viewGroup, false)
        return DataViewHolder(v)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.nameOfWorkout.text = data[position].name
        holder.cv.setOnClickListener {
            editActivityType(it, position)
        }

        holder.edit.setOnClickListener {
            editActivityType(it, position)
        }
        holder.delete.setOnClickListener {
            //TODO: warn user if a workout plan with done exercises gets deleted
            database.deleteWorkoutPlan(data[position])
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }

    private fun editActivityType(view: View, position: Int) {
        Navigation.findNavController(view).navigate(
            ListWorkoutPlansFragmentDirections.actionListWorkoutPlansFragmentToCreateEditWorkoutFragment(
                id = data[position].id!!
            )
        )
    }

    fun setData(data: MutableList<WorkoutPlan>) {
        this.data = data
    }
}
