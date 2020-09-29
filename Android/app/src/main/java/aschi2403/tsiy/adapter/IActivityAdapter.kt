package aschi2403.tsiy.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import aschi2403.tsiy.R
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.list_iactivity.view.*

class IActivityAdapter(context: Activity?, list: List<IActivity>) :
    ArrayAdapter<IActivity?>(context!!, 0, list) {
    private val repo = WorkoutRepo(getContext())

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val activity = getItem(position)

        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.list_iactivity, parent, false)
        }

        if (convertView != null) {
            val activityType =
                convertView.list_activityType as TextView

            val propertyOne =
                convertView.property_1 as TextView

            val propertyTwo =
                convertView.property_1 as TextView


            if (activity is PowerActivity) {
                activityType.text = activity.powerActivityType.name
                propertyOne.text = activity.repetitions.toString()
                propertyTwo.text = activity.weight.toString()
            }

            return convertView
        }

        return convertView as View
    }

}