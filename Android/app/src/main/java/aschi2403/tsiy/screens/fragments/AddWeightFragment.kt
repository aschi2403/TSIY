package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.data.MyDate
import aschi2403.tsiy.databinding.FragmentAddWeightBinding
import aschi2403.tsiy.helper.DatePickerFragment
import aschi2403.tsiy.model.WeightEntry
import aschi2403.tsiy.repository.WorkoutRepo
import java.text.SimpleDateFormat
import java.util.*

var public_date: MyDate? = null

const val ZERO_DOT_ONE = 0.1

class AddWeightFragment : Fragment() {
    private lateinit var binding: FragmentAddWeightBinding
    private lateinit var repo: WorkoutRepo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        public_date!!.millis = c.timeInMillis


        binding.confirmButton.setOnClickListener { confirmButton() }

        binding.addWeight.setOnClickListener {
            increaseButtonHandler()
        }

        binding.removeWeight.setOnClickListener {
            decreaseButtonHandler()
        }

        binding.datePickerButton.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(parentFragmentManager, "datePicker")
        }

        binding.dateValue.text =
            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).format(Date(public_date!!.millis)).toString()

        repo = WorkoutRepo(requireActivity())

        binding.weightValue.setText(repo.allWeightEntries.last().weight.toString())


        return binding.root
    }

    private fun decreaseButtonHandler() {
        // otherwise if the user hasn't put in a number yet and the user presses the decrease button
        // the app crashes (java.lang.NumberFormatException: empty String)
        if (!binding.weightValue.text.isNullOrEmpty()) {
            val newValue: Double = binding.weightValue.text.toString().toDouble() - ZERO_DOT_ONE

            // prevent weight value from getting negative
            if (newValue >= 0) {
                binding.weightValue.setText(
                    newValue.round().toString()
                )
            }
        }
    }

    private fun increaseButtonHandler() {
        // otherwise if the user hasn't put in a number yet and the user presses the increase button
        // the app crashes (java.lang.NumberFormatException: empty String)
        if (!binding.weightValue.text.isNullOrEmpty()) {
            binding.weightValue.setText(
                (binding.weightValue.text.toString().replace(',', '.')
                    .toDouble() + ZERO_DOT_ONE).round()
                    .toString()
            )
        } else {
            binding.weightValue.setText(
                (0.1.round()).toString()
            )
        }
    }

    private fun Double.round(decimals: Int = 2): Double =
        "%.${decimals}f".format(this).replace(',', '.').toDouble()

    private fun updateDateField() {
        binding.dateValue.text = formatDate(public_date!!.millis)
    }

    private fun formatDate(timeInMillis: Long) =
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).format(Date(timeInMillis)).toString()

    //handler for onClick of confirm button
    private fun confirmButton() {
        // get text of input field
        val text: String = binding.weightValue.text.toString()

        // parse value
        val value: Double? = text.toDoubleOrNull()

        // if value is valid add new database entry
        if (value != null) {
            val weightEntry = WeightEntry(date = public_date!!.millis, weight = value)
            repo.addWeightEntry(weightEntry)

            // navigate back to weight-fragment
            findNavController().navigate(R.id.action_addWeightFragment_to_weightFragment)
        } else {
            Toast.makeText(context, R.string.pleaseInsertACorrectWeight, Toast.LENGTH_LONG).show()
        }
    }
}

