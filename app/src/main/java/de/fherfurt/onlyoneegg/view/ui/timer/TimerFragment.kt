package de.fherfurt.onlyoneegg.view.ui.timer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false
        )

        // Get the application
        val application = requireNotNull(this.activity).application



        val timerViewModel =TimerViewModel(application)

        binding.timerViewModel = timerViewModel
        binding.setLifecycleOwner(this)


        var START_MILLI_SECONDS = 0L

        lateinit var countdown_timer: CountDownTimer
        var isRunning: Boolean = false;
        var time_in_milli_seconds = 0L

        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun pauseTimer() {

            binding.button.text = getString(R.string.timer_start)
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
            binding.button.text = getString(R.string.timer_pause)
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
            hideKeyboard(this.requireContext(), it)
        }

        binding.reset.setOnClickListener {
            resetTimer()
        }

        return binding.root
    }
}