package aschi2403.tsiy.screens;

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.home_cards.*


class AddEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_editnormalactivity)
        val newActivity = intent.extras?.getBoolean("new");
        val idOfActivity = intent.extras?.getLong("id")
        val powerActivity = intent.extras?.getBoolean("type");

        val database = WorkoutRepo(this)
        var activity: ActivityType

        val activityName = findViewById<TextInputEditText>(R.id.activityType)
        val description = findViewById<TextInputEditText>(R.id.descriptionValue)

        val cardioPoints = findViewById<TextInputEditText>(R.id.cardioPointsValue)
        val calories = findViewById<TextInputEditText>(R.id.caloriesValue)

        val icon = findViewById<ImageView>(R.id.icon)
        icon.setOnClickListener { }

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
            if (activityName.text.isNullOrEmpty() || calories.text.isNullOrEmpty() || cardioPoints.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please insert all data for the activity", Toast.LENGTH_LONG).show()
            } else {
                activity = ActivityType(
                    idOfActivity,
                    activityName.text.toString(),
                    // TODO: icon
                    "icon",
                    description.text.toString(),
                    powerActivity!!,
                    calories.text.toString().toDouble(),
                    cardioPoints.text.toString().toDouble()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (attr.data == null) {
                //Display an error
                return
            }
            //       val inputStream: InputStream = this.baseContext.getContentResolver().openInputStream(attr.data.getData())
        }
    }
}
