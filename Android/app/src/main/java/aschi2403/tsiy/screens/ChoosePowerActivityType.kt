package aschi2403.tsiy.screens

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.choose_power_activity_type.*


class ChoosePowerActivityType : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_power_activity_type)

        //TODO: save weight, repetitions
        continueButton.setOnClickListener { finish() }
    }
}
