package aschi2403.tsiy.screens

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import aschi2403.tsiy.R
import kotlinx.android.synthetic.main.pause_screen.*

class PauseScreen : AppCompatActivity(), Chronometer.OnChronometerTickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pause_screen)
        countdown.isCountDown = true
        countdown.base = SystemClock.elapsedRealtime() + 20000 //TODO: use users choice
        countdown.start()
        countdown.onChronometerTickListener = this
        var timerIsCounting = true;

        findViewById<Button>(R.id.skip).setOnClickListener { this.finish() }
        findViewById<Button>(R.id.plusmin).setOnClickListener {countdown.base += 60000}
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
                "00:00" -> {finish()}
            }
        }
    }
}