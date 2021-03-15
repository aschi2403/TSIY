package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.screens.fragments.AddEditFragment
import aschi2403.tsiy.screens.fragments.ListActivitiesFragmentDirections
import com.google.android.material.card.MaterialCardView

class ActivitiesEditDeleteAdapter(private var data: List<ActivityType>?, private val context: Context) :
        RecyclerView.Adapter<ActivitiesEditDeleteAdapter.DataViewHolder>() {

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
        return data!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
                LayoutInflater.from(viewGroup.context).inflate(R.layout.list_iactivity, viewGroup, false)
        return DataViewHolder(
                v
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        if (data != null) {
            holder.name.text = data!![position].name
            holder.delete.setOnClickListener {/*TODO: implement delete*/ }
            holder.edit.setOnClickListener { view: View ->
                Navigation.findNavController(view).navigate(ListActivitiesFragmentDirections.actionListActivitiesFragmentToFragmentAddEditFragment(id = data!![position].id!!, type = data!![position].powerActivity))
            }
        }
    }

    fun setData(data: List<ActivityType>?) {
        this.data = data
    }

}