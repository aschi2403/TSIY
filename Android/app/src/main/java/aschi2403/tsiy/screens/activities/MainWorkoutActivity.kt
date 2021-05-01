package aschi2403.tsiy.screens.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import aschi2403.tsiy.R


class MainWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_main)
        findNavController(R.id.fragNavWorkoutHost).setGraph(R.navigation.workout_nav_graph, intent.extras)
    }
}
