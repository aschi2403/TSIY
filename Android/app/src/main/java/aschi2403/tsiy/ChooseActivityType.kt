package aschi2403.tsiy

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.content_choose_activity_type.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChooseActivityType : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_type)
    }

    override fun onStart() {
        super.onStart()
        val context = this


        GlobalScope.launch {
            val repository = WorkoutRepo(context)

            val activityTypes: List<ActivityType> = repository.allActivityTypes

            val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, activityTypes)
            listview_choose_activity_type.adapter = adapter
        }
    }
}