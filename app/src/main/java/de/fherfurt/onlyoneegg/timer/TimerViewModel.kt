package de.fherfurt.onlyoneegg.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TimerViewModel(application: Application): AndroidViewModel(application) {
    val time = "now";
}