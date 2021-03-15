package aschi2403.tsiy.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aschi2403.tsiy.screens.adapters.ActivitiesEditDeleteAdapter
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentChooseActivityTypeBinding
import aschi2403.tsiy.databinding.FragmentListActivitiesBinding
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.stream.Collectors


class ListActivitiesFragment : Fragment() {

    private lateinit var editDeleteAdapter: ActivitiesEditDeleteAdapter
    private lateinit var database: WorkoutRepo
    private var type: Boolean? = false

    private lateinit var binding: FragmentListActivitiesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_list_activities, container, false
        )
        database = WorkoutRepo(requireContext())
        type = arguments?.getBoolean("type")

        val data = getData(type!!)
        binding.addNewActivity.setOnClickListener {
            findNavController().navigate(ListActivitiesFragmentDirections.actionListActivitiesFragmentToFragmentAddEditFragment(new = true, type = type!!))
        }

        val rv = binding.rv
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        rv.layoutManager = llm
        editDeleteAdapter =
                ActivitiesEditDeleteAdapter(
                        data,
                        requireContext()
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
                            getData(type!!).stream().filter { it.name.contains(query) }.collect(
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
        editDeleteAdapter.setData(getData(type!!))
        editDeleteAdapter.notifyDataSetChanged()
    }
}
