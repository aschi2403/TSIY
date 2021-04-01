package aschi2403.tsiy.screens.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentPauseScreenBinding
import kotlinx.android.synthetic.main.fragment_pause_screen.*
import java.util.concurrent.TimeUnit

class PauseScreenFragment : Fragment(), Chronometer.OnChronometerTickListener {

    private lateinit var binding: FragmentPauseScreenBinding
    private var lastCountdownPaused: Long = 0
    private var timerIsCounting = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pause_screen, container, false
        )

        val sharedPreferences: SharedPreferences =
            this.requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.countdown.isCountDown = true
        val pauseTime =
            if (sharedPreferences.getBoolean("timeUnitSeconds", true)) {
                TimeUnit.SECONDS.toMillis(sharedPreferences.getLong("pauseTime", 20))
            } else {
                TimeUnit.MINUTES.toMillis(sharedPreferences.getLong("pauseTime", 1))
            }
        binding.countdown.base = SystemClock.elapsedRealtime() + pauseTime
        binding.countdown.start()
        binding.countdown.onChronometerTickListener = this
        timerIsCounting = true


        binding.skip.setOnClickListener { findNavController().popBackStack() }
        binding.plusmin.setOnClickListener { binding.countdown.base += 60000 }
        binding.pause.setOnClickListener {
            if (timerIsCounting) {
                binding.pause.text = getString(R.string.resume)
                countdown.stop()
                lastCountdownPaused = SystemClock.elapsedRealtime()
                timerIsCounting = false
            } else {
                binding.pause.text = getString(R.string.pause)
                countdown.base = countdown.base + SystemClock.elapsedRealtime() - lastCountdownPaused
                countdown.start()
                timerIsCounting = true
            }
        }
        return binding.root
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        if (chronometer != null) {
            when (chronometer.text) {
                "00:00" -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lastCountdownPaused = SystemClock.elapsedRealtime()
        binding.countdown.stop()
    }

    override fun onResume() {
        super.onResume()
        if (timerIsCounting && lastCountdownPaused != 0L) {
            countdown.base = countdown.base + SystemClock.elapsedRealtime() - lastCountdownPaused
            countdown.start()
        }
    }

}