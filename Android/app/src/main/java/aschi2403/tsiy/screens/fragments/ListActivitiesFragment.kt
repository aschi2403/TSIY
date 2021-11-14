package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.screens.adapters.ActivitiesTypeEditDeleteAdapter
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentListViewBinding
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import java.util.*
import java.util.stream.Collectors


class ListActivitiesFragment : Fragment() {

    private lateinit var editDeleteAdapter: ActivitiesTypeEditDeleteAdapter
    private lateinit var database: WorkoutRepo
    private var managePowerActivities: Boolean? = false

    private lateinit var binding: FragmentListViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list_view, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = WorkoutRepo(requireContext())
        managePowerActivities = arguments?.getBoolean("managePowerActivities")

        val data = getData(managePowerActivities!!)
        binding.addNewActivity.setOnClickListener {
            findNavController().navigate(
                ListActivitiesFragmentDirections.actionListActivitiesFragmentToFragmentAddEditFragment(
                    new = true,
                    isPowerActivity = managePowerActivities!!
                )
            )
        }

        val rv = binding.rv
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        rv.layoutManager = llm
        editDeleteAdapter =
            ActivitiesTypeEditDeleteAdapter(
                data.toMutableList(),
                requireContext(),
                IconPackProvider(this.requireContext()).loadIconPack()
            )
        rv.adapter = editDeleteAdapter

        binding.search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                callSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String?) {
                if (query != null) {
                    editDeleteAdapter.setData(
                        getData(managePowerActivities!!).stream()
                            .filter {
                                it.name.toUpperCase(Locale.getDefault()).contains(
                                    query.toUpperCase(
                                        Locale.getDefault()
                                    )
                                )
                            }.collect(
                                Collectors.toList()
                            )
                    )
                    editDeleteAdapter.notifyDataSetChanged()
                }
            }
        })

        return binding.root
    }

    private fun getData(type: Boolean): List<ActivityType> {
        if (type) {
            return database.allPowerActivityTypes
        }
        return database.allActivityTypes
    }

    override fun onResume() {
        super.onResume()
        editDeleteAdapter.setData(getData(managePowerActivities!!).toMutableList())
        editDeleteAdapter.notifyDataSetChanged()
    }
}
