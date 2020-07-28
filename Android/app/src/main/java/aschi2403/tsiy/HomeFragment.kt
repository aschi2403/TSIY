package aschi2403.tsiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import aschi2403.tsiy.model.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        val context = activity as MainActivity
        val repository = WorkoutRepo(context)

        GlobalScope.launch {
            // get data
            val activities: List<IActivity> = repository.allPowerActivities

            // get adapter with MainActivity as context
            val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, activities)

            listview_home.adapter = adapter
        }
    }
}