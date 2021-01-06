package aschi2403.tsiy.screens;

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.util.StringUtil
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.textfield.TextInputEditText

class AddEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_editnormalactivity)
        val newActivity = intent.extras?.getBoolean("new");
        val idOfActivity = intent.extras?.getLong("id")
        val powerActivity = intent.extras?.getBoolean("type");

        val database = WorkoutRepo(this)
        var activity: ActivityType

        //TODO: provide an input for cardio points and calories

        val activityName = findViewById<TextInputEditText>(R.id.activityType)
        val description = findViewById<TextInputEditText>(R.id.descriptionValue)

        if (!newActivity!! && idOfActivity != null) {
            activity = if (powerActivity!!) {
                database.powerActivityTypeById(idOfActivity)
            } else {
                database.activityTypeById(idOfActivity)
            }

            activityName.setText(activity.name)
            // TODO: set icon
            description.setText(activity.description)
        }

        findViewById<Button>(R.id.close).setOnClickListener { finish() }
        findViewById<Button>(R.id.save).setOnClickListener {
            if (activityName.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please insert a name for the activity", Toast.LENGTH_LONG).show()
            } else {
                activity = ActivityType(
                    idOfActivity,
                    activityName.text.toString(),
                    // TODO: icon
                    "icon",
                    description.text.toString(),
                    powerActivity!!,
                    10.0, //TODO: change
                    10.0 //TODO: change
                )
                if (!newActivity && idOfActivity != null) {
                    database.updateActivityType(activity)
                } else {
                    activity.id = null
                    database.addActivityType(activity)
                }
                finish()
            }
        }
    }
}
