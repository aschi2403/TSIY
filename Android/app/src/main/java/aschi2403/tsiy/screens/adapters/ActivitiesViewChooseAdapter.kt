package aschi2403.tsiy.screens.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.screens.activities.MainWorkoutActivity
import com.google.android.material.card.MaterialCardView
import com.maltaisn.icondialog.pack.IconPack


class ActivitiesViewChooseAdapter(
    private var data: MutableList<ActivityType>,
    private val context: Context,
    private val iconPack: IconPack
) :
    RecyclerView.Adapter<ActivitiesViewChooseAdapter.DataViewHolder>() {

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
        holder.icon.setImageDrawable(iconPack.getIcon(data[position].icon)!!.drawable)
        holder.cv.setOnClickListener { view: View ->
            val intent = Intent(view.context, MainWorkoutActivity::class.java)
            intent.putExtra("activityTypeId", data[position].id)
            intent.putExtra("name", data[position].name)
            intent.putExtra("type", data[position].isPowerActivity)
            context.startActivity(intent)
        }
    }

    fun setData(data: MutableList<ActivityType>) {
        this.data = data
    }
}