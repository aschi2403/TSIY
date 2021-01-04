package aschi2403.tsiy.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.ChooseActivityType
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        binding.startNewActivity.setOnClickListener { onAdd() }

        return binding.root
    }

    private fun onAdd() {
        val i = Intent(requireView().context, ChooseActivityType::class.java)
        startActivity(i)
    }

}