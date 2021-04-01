package aschi2403.tsiy.screens.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.data.MyDate
import aschi2403.tsiy.databinding.FragmentAddWeightBinding
import aschi2403.tsiy.model.WeightEntry
import aschi2403.tsiy.repository.WorkoutRepo
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
var public_date: MyDate? = null

class AddWeightFragment : Fragment() {
    private lateinit var binding: FragmentAddWeightBinding
    private var repo: WorkoutRepo? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_weight, container, false
        )

        public_date = MyDate(callBack = this::updateDateField)

        // Use the current date as the default date
        val c = Calendar.getInstance()
        public_date!!.year = c.get(Calendar.YEAR)
        public_date!!.month = c.get(Calendar.MONTH)
        public_date!!.day = c.get(Calendar.DAY_OF_MONTH)
        public_date!!.time_millis = c.timeInMillis


        binding.confirmButton.setOnClickListener { confirmButton() }

        binding.addWeight.setOnClickListener {
            increaseButtonHandler()
        }

        binding.removeWeight.setOnClickListener {
            decreaseButtonHandler()
        }

        binding.datePickerButton.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(requireFragmentManager(), "datePicker")
        }

        binding.dateValue.text =
            SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(public_date!!.time_millis)).toString()

        repo = activity?.let { WorkoutRepo(it) }

        if (!repo?.allWeightEntries.isNullOrEmpty()) {
            binding.weightValue.setText(repo?.allWeightEntries?.last()?.weight.toString())
        }

        return binding.root
    }

    private fun decreaseButtonHandler() {
        // otherwise if the user hasn't put in a number yet and the user presses the decrease button the app crashes (java.lang.NumberFormatException: empty String)
        if (!binding.weightValue.text.isNullOrEmpty()) {
            val newValue: Double = binding.weightValue.text.toString().toDouble() - 0.1

            // prevent weight value from getting negative
            if (newValue >= 0) {
                binding.weightValue.setText(
                    newValue.round().toString()
                )
            }
        }
    }

    private fun increaseButtonHandler() {
        // otherwise if the user hasn't put in a number yet and the user presses the increase button the app crashes (java.lang.NumberFormatException: empty String)
        if (!binding.weightValue.text.isNullOrEmpty()) {
            binding.weightValue.setText(
                (binding.weightValue.text.toString().toDouble() + 0.1).round().toString()
            )
        } else {
            binding.weightValue.setText(
                (0.1.round()).toString()
            )
        }
    }

    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    fun updateDateField() {
        binding.dateValue.setText(formatDate(public_date!!.time_millis))
    }

    private fun formatDate(time_millis: Long) = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(time_millis)).toString()

    //handler for onClick of confirm button
    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmButton() {
        // get text of input field
        val text: String = binding.weightValue.text.toString()

        // parse value
        val value: Double? = text.toDoubleOrNull()

        // if value is valid add new database entry
        if (value != null) {
            val weightEntry = WeightEntry(date = public_date!!.time_millis, weight = value)
            repo?.addWeightEntry(weightEntry)

            // navigate back to weight-fragment
            findNavController().navigate(R.id.action_addWeightFragment_to_weightFragment)
        } else {
            Toast.makeText(context, "Please insert a correct weight", Toast.LENGTH_LONG).show()
        }

    }
}

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
        public_date!!.time_millis = calendar.timeInMillis
    }
}
