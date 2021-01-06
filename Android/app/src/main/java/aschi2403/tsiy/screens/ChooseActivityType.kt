package aschi2403.tsiy.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.ActivitiesViewChooseAdapter
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo

class ChooseActivityType : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_activity_type)

        val database = WorkoutRepo(this)


        val rv = findViewById<RecyclerView>(R.id.listActivitiesRV)
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rv.layoutManager = llm
        val mergedList = ArrayList<ActivityType>()
        mergedList.addAll(database.allPowerActivityTypes)
        mergedList.addAll(database.allActivityTypes)
        val adapter = ActivitiesViewChooseAdapter(mergedList, this);
        rv.adapter = adapter;
    }
}