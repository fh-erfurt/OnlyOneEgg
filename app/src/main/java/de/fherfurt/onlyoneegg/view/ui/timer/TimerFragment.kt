package de.fherfurt.onlyoneegg.view.ui.timer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentTimerBinding
import java.util.*

/*
* A Fragment for our Timer
* Where you can input the minutes:seconds
* With a start, pause and stop button
* */
class TimerFragment : Fragment() {

    // Enum used for saving the state of the timer
    enum class TimerState {
        Stopped, Paused, Running
    }

    // Define variables that are needed in the whole fragment
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0

    private var startWasAlreadyPressed: Boolean = false

    private lateinit var binding: FragmentTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set the Fragment as only Portrait
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false
        )

        // Get the application
        val application = requireNotNull(this.activity).application

        // Setup the viewModel and binding
        val timerViewModel = TimerViewModel(application)
        binding.timerViewModel = timerViewModel
        binding.setLifecycleOwner(this)

        // Helper Function for hiding the keyboard
        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // If the user clicks ok on the keyboard -> close it
        binding.editTextTime.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(requireContext(), requireView())
                return@OnEditorActionListener true
            }
            false
        })

        // The user presses the start timer button
        binding.startButton.setOnClickListener {
            // Check if the user has even inputted something
            if (binding.editTextTime.text.toString().trim().isNotEmpty()) {
                // We need to check if the edit field is visible else the timer should not reset/update
                if (binding.editTextTime.visibility == Button.VISIBLE) {
                    binding.timer.text = binding.editTextTime.text

                    // Set the time in the text field
                    val time = binding.editTextTime.text.toString().split(":").toList()
                    secondsRemaining = time.first().toLong() * 60
                    if (time.size == 2) {
                        secondsRemaining += time.last().toLong()
                    }

                    switchEditAndTextView()

                    context?.let { view?.let { it1 -> hideKeyboard(it, it1) } }
                }
                startTimer()
                timerState = TimerState.Running
                updateButtons()
                startWasAlreadyPressed = true
            }
            else { // If the user has not written any time in the edit field
               Toast.makeText(context, "Please input a time", Toast.LENGTH_SHORT).show()
            }
        }

        // The user presses the pause button
        binding.pauseButton.setOnClickListener {
            startWasAlreadyPressed = false
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        // The user presses the stop button
        binding.stopButton.setOnClickListener {
            startWasAlreadyPressed = false
            timer.cancel()
            onTimerFinished()
            switchEditAndTextView()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initTimer()
    }

    private fun initTimer() {
        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped) {
            secondsRemaining = timerLengthSeconds
        }

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        if (startWasAlreadyPressed) {
            // play a alarm sound for the user
            val mp: MediaPlayer = MediaPlayer.create(context, R.raw.alarmclock)
            mp.start()
        }
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running

        //  setup our timer with needed function overrides
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.timer.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }

    // Switch the visibility of the edit field and the text view
    private fun switchEditAndTextView() {
        if (binding.editTextTime.visibility == Button.INVISIBLE) {
            binding.editTextTime.visibility = Button.VISIBLE
        } else {
            binding.editTextTime.visibility = Button.INVISIBLE
        }
        if (binding.timer.visibility == Button.INVISIBLE) {
            binding.timer.visibility = Button.VISIBLE
        } else {
            binding.timer.visibility = Button.INVISIBLE
        }
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                binding.startButton.isEnabled = false
                binding.pauseButton.isEnabled = true
                binding.stopButton.isEnabled = true
            }
            TimerState.Stopped -> {
                binding.startButton.isEnabled = true
                binding.pauseButton.isEnabled = false
                binding.stopButton.isEnabled = false
            }
            TimerState.Paused -> {
                binding.startButton.isEnabled = true
                binding.pauseButton.isEnabled = false
                binding.stopButton.isEnabled = true
            }
        }
    }
}