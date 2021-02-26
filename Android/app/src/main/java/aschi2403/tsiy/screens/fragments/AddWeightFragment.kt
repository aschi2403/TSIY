package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentAddWeightBinding
import aschi2403.tsiy.model.WeightEntry
import aschi2403.tsiy.repository.WorkoutRepo
import kotlinx.android.synthetic.main.fragment_add_weight.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddWeightFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddWeightFragment : Fragment() {
    private lateinit var binding: FragmentAddWeightBinding
    private var repo: WorkoutRepo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_weight, container, false
        )

        binding.confirmButton.setOnClickListener { confirm_button() }

        repo = activity?.let { WorkoutRepo(it) }

        return binding.root
    }

    //handler for onClick of confirm button
    fun confirm_button() {
        // get text if input field
        val text: String = binding.weightValue.text.toString()

        // parse value
        val value: Double? = text.toDoubleOrNull()

        // if value is valid add new database entry
        if (value != null) {
            val weightEntry = WeightEntry(date = System.currentTimeMillis(), weight = value)

            repo?.addWeightEntry(weightEntry)
        }

        // navigate back to weight-fragment
        findNavController().navigate(R.id.action_addWeightFragment_to_weightFragment)
    }
}