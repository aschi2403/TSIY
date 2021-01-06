package aschi2403.tsiy.screens;

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.ActivitiesEditDeleteAdapter
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListActivities : AppCompatActivity() {

    private lateinit var editDeleteAdapter: ActivitiesEditDeleteAdapter
    private lateinit var database: WorkoutRepo
    private var type: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_activities);
        database = WorkoutRepo(this)
        type = intent.extras?.getBoolean("type")

        val data = getData(type!!)
        findViewById<FloatingActionButton>(R.id.addNewActivity).setOnClickListener {
            val intent = Intent(
                this,
                AddEditActivity::class.java
            )
            intent.putExtra("type", type!!)
            intent.putExtra("id", -1)
            intent.putExtra("new", true)
            startActivity(intent)
        }
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rv.layoutManager = llm
        editDeleteAdapter = ActivitiesEditDeleteAdapter(data, this)
        rv.adapter = editDeleteAdapter;
    }

    private fun getData(type: Boolean): List<ActivityType> {
        if (type) {
            return database.allPowerActivityTypes
        }
        return database.allActivityTypes;
    }

    override fun onResume() {
        super.onResume()
        editDeleteAdapter.setData(getData(type!!))
        editDeleteAdapter.notifyDataSetChanged()
    }
}
