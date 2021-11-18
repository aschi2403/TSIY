package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentHomeBinding
import aschi2403.tsiy.helper.DataMerger
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.adapters.HomeListAdapter
import aschi2403.tsiy.screens.models.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*


class HomeFragment : Fragment() {

    private var checkedItem: Int = 0
    private lateinit var database: WorkoutRepo
    private lateinit var homeListAdapter: HomeListAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var dataMerger: DataMerger

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        (activity as AppCompatActivity?)!!.filterButtonAppBar.setOnClickListener {
            with(DialogView(this.requireContext())) {
                showItemCheckDialog(R.string.filterDoneActivities, listOf(
                    requireContext().getString(R.string.all),
                    requireContext().getString(R.string.generalActivity),
                    requireContext().getString(R.string.powerActivity)
                ), { _, _ ->
                    homeListAdapter.setData(
                        dataMerger.getData(
                            checkedItem,
                            getString(R.string.workout)
                        )
                    )
                    homeListAdapter.notifyDataSetChanged()
                    this.checkedItem = checkedItem
                }, { _, _ -> })
            }
        }

        (activity as AppCompatActivity?)!!.filterButtonAppBar.visibility = View.VISIBLE

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        binding.startNewActivity.setOnClickListener {
            if (binding.startGeneralActivity.isVisible) {
                closeFloatingButtons()
            } else {
                showFloatingButtons()
            }
        }

        binding.startGeneralActivity.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToChooseActivityFragment(
                    generalActivity = true
                )
            )
        }
        binding.startPowerActivity.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToChooseActivityFragment(
                    generalActivity = false
                )
            )
        }
        binding.startWorkout.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToChooseWorkoutPlanFragment())
        }

        database = WorkoutRepo(this.requireContext())

        dataMerger = DataMerger(database)

        val rv = binding.listOfActivities
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(this.context)

        rv.layoutManager = llm
        homeListAdapter = HomeListAdapter(
            dataMerger.getData(checkedItem, getString(R.string.workout)),
            this.requireContext(),
            IconPackProvider(this.requireContext()).loadIconPack(),
            checkedItem,
            true
        )
        rv.adapter = homeListAdapter
        return binding.root
    }

    private fun showFloatingButtons() {
        changeVisibilityOfFloatingButtons(View.VISIBLE)
        binding.startNewActivity.setImageDrawable(
            ContextCompat.getDrawable(
                this.requireContext(),
                R.drawable.ic_baseline_close_24
            )
        )
    }

    private fun closeFloatingButtons() {
        changeVisibilityOfFloatingButtons(View.GONE)
        binding.startNewActivity.setImageDrawable(
            ContextCompat.getDrawable(
                this.requireContext(),
                R.drawable.ic_baseline_add_24
            )
        )
    }

    private fun getData(): MutableList<IActivity> {
        return when (checkedItem) {
            0 -> {
                val mergedList =
                    database.allPowerActivities.plus(database.allGeneralActivities)
                mergedList.sortedByDescending { it.startDate }.toMutableList()
            }
            1 -> {
                database.allGeneralActivities.sortedByDescending { it.startDate }
                    .toMutableList()
            }
            else -> {
                database.allPowerActivities.sortedByDescending { it.startDate }
                    .toMutableList()
            }
        }
    }

    private fun changeVisibilityOfFloatingButtons(visibility: Int) {
        binding.startGeneralActivity.visibility = visibility
        binding.startPowerActivity.visibility = visibility
        binding.startWorkout.visibility = visibility
    }

    override fun onResume() {
        super.onResume()
        homeListAdapter.setData(dataMerger.getData(checkedItem, getString(R.string.workout)))
        homeListAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.filterButtonAppBar.visibility = View.GONE
    }
}
