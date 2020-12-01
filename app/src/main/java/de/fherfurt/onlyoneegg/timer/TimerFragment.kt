package de.fherfurt.onlyoneegg.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false
        )

        // Get the application
        val application = requireNotNull(this.activity).application

        // Spawn the viewModelFactory
        val viewModelFactory = TimerViewModelFactory(application)

        val timerViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TimerViewModel::class.java)

        binding.timerViewModel = timerViewModel
        binding.setLifecycleOwner(this)


        var START_MILLI_SECONDS = 60000L

        lateinit var countdown_timer: CountDownTimer
        var isRunning: Boolean = false;
        var time_in_milli_seconds = 0L


        fun pauseTimer() {

            binding.button.text = "Start"
            countdown_timer.cancel()
            isRunning = false
            binding.reset.visibility = View.VISIBLE
        }

        fun updateTextUI() {
            val minute = (time_in_milli_seconds / 1000) / 60
            val seconds = (time_in_milli_seconds / 1000) % 60

            binding.timer.text = "$minute:$seconds"
        }

        fun resetTimer() {
            time_in_milli_seconds = START_MILLI_SECONDS
            updateTextUI()
            binding.reset.visibility = View.INVISIBLE
        }

        fun startTimer(time_in_seconds: Long) {
            countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
                override fun onFinish() {
                    // TODO do something after timer finishes
                }

                override fun onTick(p0: Long) {
                    time_in_milli_seconds = p0
                    updateTextUI()
                }
            }
            countdown_timer.start()

            isRunning = true
            binding.button.text = "Pause"
            binding.reset.visibility = View.INVISIBLE

        }

        binding.button.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                val time = binding.timeEditText.text.toString()
                time_in_milli_seconds = time.toLong() * 60000L
                startTimer(time_in_milli_seconds)
            }
        }

        binding.reset.setOnClickListener {
            resetTimer()
        }

        return binding.root
    }
}