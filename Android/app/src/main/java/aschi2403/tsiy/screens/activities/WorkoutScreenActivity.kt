package aschi2403.tsiy.screens.activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.activity_workout_screen.*


class WorkoutScreenActivity : AppCompatActivity() {
    private lateinit var database: WorkoutRepo
    private var idOfActivity: Long = -1
    private var set = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_screen)
        val activityTypeId = intent.extras?.getLong("activityTypeId")!!

        database = WorkoutRepo(this)


        activity.text = intent.extras?.getString("name")
        if (!intent.extras?.getBoolean("type")!!) { //normal activity
            next.visibility = View.INVISIBLE
            idOfActivity = database.addGeneralActivity(
                GeneralActivity(
                    date = System.currentTimeMillis(),
                    activityTypeId = activityTypeId
                )
            )!!
        } else {
            idOfActivity = database.addPowerActivity(
                PowerActivity(
                    date = System.currentTimeMillis(),
                    activityTypeId = activityTypeId
                )
            )!!
            next.setOnClickListener {
                set++;
                val intent = Intent(this, ChoosePowerActivityType::class.java)
                intent.putExtra("idOfPowerActivity", idOfActivity)
                startActivityForResult(intent, 1);
            }
        }

        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        pause.setOnClickListener {
            pause()
        }
        close.setOnClickListener {
            saveInDatabase(activityTypeId)
            finish()
        }
    }

    private fun saveInDatabase(activityTypeId: Long) {
        val type = intent.extras?.getBoolean("type")!!
        if (!type) { // normal activity
            database.updateActivity(
                GeneralActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    date = database.generalActivityById(idOfActivity).date,
                    time = SystemClock.elapsedRealtime() - timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0
                )
            ) //TODO: calculate cardioPoints and calories
        } else { // power activity
            val intent = Intent(this, ChoosePowerActivityType::class.java)
            intent.putExtra("idOfPowerActivity", idOfActivity)
            startActivity(intent)
            database.updatePowerActivity(
                PowerActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    date = database.powerActivityById(idOfActivity).date,
                    time = SystemClock.elapsedRealtime() - timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    sets = set
                )
            )
        }
    }

    private fun pause() {
        timer.stop() //TODO: timer isn't stopping
        val intent = Intent(this, PauseScreenActivity::class.java)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            pause()
        } else {
            timer.start()
        }
    }
}