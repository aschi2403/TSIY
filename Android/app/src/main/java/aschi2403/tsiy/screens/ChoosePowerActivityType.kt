package aschi2403.tsiy.screens

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.SetEntry
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.choose_power_activity_type.*
import org.w3c.dom.Text


class ChoosePowerActivityType : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_power_activity_type)
        val idOfPowerActivity = intent.extras?.getLong("idOfPowerActivity")!!

        continueButton.setOnClickListener {
            if (weightValue.text.isNullOrEmpty() || repetitionsValue.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please insert all data for the activity", Toast.LENGTH_LONG).show()
            } else {
                saveDataInDatabase(idOfPowerActivity)
                finish()
            }
        }
    }

    private fun saveDataInDatabase(idOfPowerActivity: Long) {
        val database = WorkoutRepo(this)
        database.insertSetEntry(
            SetEntry(
                weight = weightValue.text.toString().toDouble(),
                repetitions = repetitionsValue.text.toString().toInt(),
                powerActivityId = idOfPowerActivity
            )
        )
    }
}
