package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.adapters.HomeListAdapter
import aschi2403.tsiy.screens.models.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*


class HomeFragment : Fragment() {

    private var checkedItem: Int = 0
    private lateinit var database: WorkoutRepo
    private lateinit var editDeleteAdapter: HomeListAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        (activity as AppCompatActivity?)!!.filterButtonAppBar.setOnClickListener { showDialog() }

        (activity as AppCompatActivity?)!!.filterButtonAppBar.visibility = View.VISIBLE

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        binding.startNewActivity.setOnClickListener {
            if (binding.startGeneralActivity.isVisible) {
                binding.startGeneralActivity.visibility = View.GONE
                binding.startPowerActivity.visibility = View.GONE
                binding.startWorkout.visibility = View.GONE
                binding.startNewActivity.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.ic_baseline_add_24
                    )
                )
            } else {
                binding.startGeneralActivity.visibility = View.VISIBLE
                binding.startPowerActivity.visibility = View.VISIBLE
                binding.startWorkout.visibility = View.VISIBLE
                binding.startNewActivity.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.ic_baseline_close_24
                    )
                )
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

    override fun onResume() {
        super.onResume()
        editDeleteAdapter.setData(getData())
        editDeleteAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.filterButtonAppBar.visibility = View.GONE
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.filterDoneActivities)

        val items = arrayOf<CharSequence>(
            context!!.getString(R.string.all),
            context!!.getString(R.string.generalActivity),
            context!!.getString(R.string.powerActivity)
        )

        builder.setSingleChoiceItems(
            items, checkedItem
        ) { _, which ->
            checkedItem = which
        }
        builder.setPositiveButton(R.string.filter) { _, _ ->
            editDeleteAdapter.setData(getData())
            editDeleteAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton(R.string.cancel, null)

        val dialog = builder.create()
        dialog.show()
    }
}