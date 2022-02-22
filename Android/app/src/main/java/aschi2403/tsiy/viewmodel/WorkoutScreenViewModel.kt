package aschi2403.tsiy.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel

class WorkoutScreenViewModel : ViewModel() {
    val values = Bundle()
    var startDate: Long = 0
    val donePowerActivities: HashMap<Long, Long> = HashMap()
}
