package aschi2403.tsiy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import aschi2403.tsiy.databinding.FragmentAddWeightBinding
import aschi2403.tsiy.databinding.FragmentWeightBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddWeightFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddWeightFragment : Fragment() {
    private lateinit var binding: FragmentAddWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_weight, container, false
        )
        return binding.root
    }
}