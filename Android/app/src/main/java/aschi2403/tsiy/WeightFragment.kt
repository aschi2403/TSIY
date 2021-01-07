package aschi2403.tsiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.databinding.FragmentWeightBinding


class WeightFragment : Fragment() {
    private lateinit var binding: FragmentWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weight, container, false
        )
        binding.addWeight
        return binding.root
    }

}