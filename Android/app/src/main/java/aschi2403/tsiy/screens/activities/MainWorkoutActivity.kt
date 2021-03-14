package aschi2403.tsiy.screens.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import aschi2403.tsiy.R
import kotlinx.android.synthetic.main.activity_main.*


class MainWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_main)

        /*  intent.extras.getLong("activityTypeId")
          intent.extras.getString("name")
          intent.extras.getBoolean("type")*/


        findNavController(R.id.fragNavWorkoutHost).setGraph(R.navigation.workout_nav_graph, intent.extras)
    }
}
