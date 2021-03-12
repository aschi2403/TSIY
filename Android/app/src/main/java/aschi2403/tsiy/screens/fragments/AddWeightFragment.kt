package aschi2403.tsiy.screens.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentAddWeightBinding
import aschi2403.tsiy.model.WeightEntry
import aschi2403.tsiy.repository.WorkoutRepo
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 */
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

        binding.confirmButton.setOnClickListener { confirmButton() }

        binding.addWeight.setOnClickListener {
            binding.weightValue.setText(
                (binding.weightValue.text.toString().toDouble() + 0.1).round().toString()
            )
        }

        binding.removeWeight.setOnClickListener {
            binding.weightValue.setText(
                (binding.weightValue.text.toString().toDouble() - 0.1).round().toString()
            )
        }

        binding.dateValue.setText(SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(currentTimeMillis())).toString())

        repo = activity?.let { WorkoutRepo(it) }

        if (!repo?.allWeightEntries.isNullOrEmpty()) {
            binding.weightValue.setText(repo?.allWeightEntries?.last()?.weight.toString())
        }

        return binding.root
    }

    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    //handler for onClick of confirm button
    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmButton() {
        // get text if input field
        val text: String = binding.weightValue.text.toString()

        // parse value
        val value: Double? = text.toDoubleOrNull()

        // if value is valid add new database entry
        if (value != null) {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN)
            val date = sdf.parse(binding.dateValue.text.toString())!!
            val millis = date.time
            val weightEntry = WeightEntry(date = millis, weight = value)

            repo?.addWeightEntry(weightEntry)

            // navigate back to weight-fragment
            findNavController().navigate(R.id.action_addWeightFragment_to_weightFragment)
        } else {
            Toast.makeText(context, "Please insert a correct weight", Toast.LENGTH_LONG).show()
        }

    }
}