package aschi2403.tsiy.screens.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import kotlinx.android.synthetic.main.activity_pause_screen.*
import java.util.concurrent.TimeUnit

class PauseScreenActivity : AppCompatActivity(), Chronometer.OnChronometerTickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause_screen)

        val sharedPreferences: SharedPreferences =
            this.baseContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

        countdown.isCountDown = true
        val pauseTime =
            if (sharedPreferences.getBoolean("timeUnitSeconds", true)) {
                TimeUnit.SECONDS.toMillis(sharedPreferences.getLong("pauseTime", 20))
            } else {
                TimeUnit.MINUTES.toMillis(sharedPreferences.getLong("pauseTime", 1))
            }
        countdown.base = SystemClock.elapsedRealtime() + pauseTime
        countdown.start()
        countdown.onChronometerTickListener = this
        var timerIsCounting = true;

        findViewById<Button>(R.id.skip).setOnClickListener { this.finish() }
        findViewById<Button>(R.id.plusmin).setOnClickListener { countdown.base += 60000 }
        val pause = findViewById<Button>(R.id.pause)
        pause.setOnClickListener {
            if (timerIsCounting) {
                pause.text = "Resume" //TODO: extract string resource
                countdown.stop()
                timerIsCounting = false;
            } else {
                pause.text = "Pause" //TODO: extract string resource
                countdown.start()
                timerIsCounting = true;
            }
        }
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        if (chronometer != null) {
            when (chronometer.text) {
                "00:00" -> {
                    finish()
                }
            }
        }
    }
}