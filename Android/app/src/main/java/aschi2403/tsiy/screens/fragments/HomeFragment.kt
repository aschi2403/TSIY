package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.screens.adapters.HomeListAdapter
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentHomeBinding
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.models.HomeViewModel
import java.util.stream.Collectors


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var database: WorkoutRepo
    private lateinit var editDeleteAdapter: HomeListAdapter
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

        database = WorkoutRepo(this.requireContext())


        val rv = binding.listOfActivities
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(this.context)

        rv.layoutManager = llm
        editDeleteAdapter = HomeListAdapter(
            getData(),
            this.requireContext(),
            IconPackProvider(this.requireContext()).loadIconPack()
        )
        rv.adapter = editDeleteAdapter
        return binding.root
    }

    private fun onAdd() {
        findNavController().navigate(R.id.action_homeFragment_to_chooseActivityFragment)
    }

    private fun getData(): List<IActivity> {
        val mergedList = ArrayList<IActivity>()
        mergedList.addAll(database.allPowerActivities)
        mergedList.addAll(database.allGeneralActivities)
        return mergedList.stream().sorted(Comparator.comparing(IActivity::startDate).reversed()).collect(Collectors.toList())
    }

    override fun onResume() {
        super.onResume()
        editDeleteAdapter.setData(getData())
        editDeleteAdapter.notifyDataSetChanged()
    }

}