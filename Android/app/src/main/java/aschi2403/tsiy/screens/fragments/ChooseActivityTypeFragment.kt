package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aschi2403.tsiy.screens.adapters.ActivitiesViewChooseAdapter
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentChooseActivityTypeBinding
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity

class ChooseActivityTypeFragment : Fragment() {

    private lateinit var binding: FragmentChooseActivityTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_activity_type, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val database = context?.let { WorkoutRepo(it) }!!


        val rv = binding.listActivitiesRV
        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        rv.layoutManager = llm
        val mergedList = ArrayList<ActivityType>()
        mergedList.addAll(database.allPowerActivityTypes)
        mergedList.addAll(database.allActivityTypes)

        if (mergedList.isEmpty()) {
            binding.noActivityInfoText.visibility = View.VISIBLE
            binding.gotToSettings.visibility = View.VISIBLE
            binding.gotToSettings.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_choose_activity_type_to_settingsFragment)
            }
        }
        val adapter =
            ActivitiesViewChooseAdapter(
                mergedList,
                requireContext()
            )
        rv.adapter = adapter
        return binding.root
    }
}