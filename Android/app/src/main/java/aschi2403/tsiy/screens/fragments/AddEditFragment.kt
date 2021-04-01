package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentEditnormalactivityBinding
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import com.maltaisn.icondialog.IconDialog
import com.maltaisn.icondialog.IconDialogSettings
import com.maltaisn.icondialog.data.Icon
import com.maltaisn.icondialog.pack.IconPack


class AddEditFragment : Fragment(), IconDialog.Callback {

    private var iconId: Int = 522 // Cycling icon as standard
    private lateinit var binding: FragmentEditnormalactivityBinding

    private lateinit var iconPack: IconPack

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_editnormalactivity, container, false
        )

        iconPack = IconPackProvider(this.requireContext()).loadIconPack()

        val newActivity = arguments?.getBoolean("new")
        val idOfActivity = arguments?.getLong("id")
        val powerActivity = arguments?.getBoolean("type")

        val database = WorkoutRepo(requireContext())
        var activity: ActivityType


        val iconDialog = childFragmentManager.findFragmentByTag("ICON_DIALOG_TAG") as IconDialog?
            ?: IconDialog.newInstance(IconDialogSettings())





        if (!newActivity!! && idOfActivity != null) {
            activity = if (powerActivity!!) {
                database.powerActivityTypeById(idOfActivity)
            } else {
                database.activityTypeById(idOfActivity)
            }
            binding.activityType.setText(activity.name)
            binding.descriptionValue.setText(activity.description)
            iconId = activity.icon
            binding.caloriesValue.setText(activity.caloriesPerMinute.toString())
            binding.cardioPointsValue.setText(activity.cardioPointsPerMinute.toString())

        }

        binding.icon.setOnClickListener { iconDialog.show(childFragmentManager, "ICON_DIALOG_TAG") }
        binding.icon.setImageDrawable(iconPack.getIcon(iconId)!!.drawable)

        binding.close.setOnClickListener { findNavController().popBackStack() }
        binding.save.setOnClickListener {
            if (binding.activityType.text.isNullOrEmpty() || binding.caloriesValue.text.isNullOrEmpty() || binding.cardioPointsValue.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please insert all data for the activity", Toast.LENGTH_LONG).show()
            } else {
                activity = ActivityType(
                    idOfActivity,
                    binding.activityType.text.toString(),
                    iconId,
                    binding.descriptionValue.text.toString(),
                    powerActivity!!,
                    binding.caloriesValue.text.toString().toDouble(),
                    binding.cardioPointsValue.text.toString().toDouble()
                )
                if (!newActivity && idOfActivity != null) {
                    database.updateActivityType(activity)
                } else {
                    activity.id = null
                    database.addActivityType(activity)
                }
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override val iconDialogIconPack: IconPack?
        get() = iconPack

    override fun onIconDialogIconsSelected(dialog: IconDialog, icons: List<Icon>) {
        iconId = icons.first().id
        binding.icon.setImageDrawable(iconPack.getIcon(iconId)!!.drawable)
    }
}
