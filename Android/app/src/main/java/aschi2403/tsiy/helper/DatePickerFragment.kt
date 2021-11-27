package aschi2403.tsiy.helper

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import aschi2403.tsiy.screens.fragments.public_date
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = public_date!!.year
        val month = public_date!!.month
        val day = public_date!!.day

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        public_date!!.day = day
        public_date!!.month = month
        public_date!!.year = year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        public_date!!.millis = calendar.timeInMillis
    }
}
