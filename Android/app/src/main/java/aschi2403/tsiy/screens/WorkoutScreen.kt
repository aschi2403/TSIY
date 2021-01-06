package aschi2403.tsiy.screens

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.workout_screen.*

class WorkoutScreen : AppCompatActivity() {
    var set = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workout_screen)
        activity.text = intent.extras?.getString("name")
        if (!intent.extras?.getBoolean("type")!!) { //normal activity
            next.visibility = View.INVISIBLE
        } else {
            next.setOnClickListener {
                set++;
                val intent = Intent(this, ChoosePowerActivityType::class.java)
                startActivityForResult(intent, 1);
            }
        }

        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        pause.setOnClickListener {
            pause()
        }
        close.setOnClickListener {
            saveInDatabase()
            finish()
        }
    }

    private fun saveInDatabase() {
        val database = WorkoutRepo(this)
        val id = intent.extras?.getLong("id")!!
        val type = intent.extras?.getBoolean("type")!!
        if (!type) { //normal activity
            database.addGeneralActivity(
                GeneralActivity(
                    null,
                    id,
                    timer.base,
                    0.0,
                    0.0
                )
            ); //TODO: calculate cardioPoints and calories
        } else {
            val intent = Intent(this, ChoosePowerActivityType::class.java)
            intent.putExtra("id", id)
            intent.putExtra("type", type)
            startActivity(intent)
        }
    }

    private fun pause() {
        timer.stop() //TODO: timer isn't stopping
        val intent = Intent(this, PauseScreen::class.java)
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