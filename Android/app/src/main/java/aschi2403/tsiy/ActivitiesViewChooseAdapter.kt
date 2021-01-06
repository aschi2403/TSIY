package aschi2403.tsiy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.screens.AddEditActivity
import aschi2403.tsiy.screens.WorkoutScreen
import com.google.android.material.card.MaterialCardView

class ActivitiesViewChooseAdapter(private var data: List<ActivityType>?, private val context: Context) :
    RecyclerView.Adapter<ActivitiesViewChooseAdapter.DataViewHolder>() {

    class DataViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon //TODO: implement
                : ImageView
        val name: TextView
        val cv: MaterialCardView

        init {
            cv = itemView.findViewById(R.id.iactivity_card)
            name = itemView.findViewById(R.id.nameOfActivity)
            icon = itemView.findViewById(R.id.imageOfActivity)
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.list_of_activities, viewGroup, false)
        return DataViewHolder(v)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        if (data != null) {
            holder.name.text = data!![position].name
            holder.cv.setOnClickListener { view: View ->
                val intent = Intent(view.context, WorkoutScreen::class.java)
                intent.putExtra("id", data!![position].id)
                intent.putExtra("name", data!![position].name)
                intent.putExtra("type", data!![position].powerActivity)
                context.startActivity(intent)
            }
        }
    }

    fun setData(data: List<ActivityType>?) {
        this.data = data
    }

}