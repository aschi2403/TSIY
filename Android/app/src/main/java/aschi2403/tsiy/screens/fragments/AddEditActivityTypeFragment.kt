package aschi2403.tsiy.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentAddeditactivitytypeBinding
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.helper.IconPackProvider
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import com.maltaisn.icondialog.IconDialog
import com.maltaisn.icondialog.IconDialogSettings
import com.maltaisn.icondialog.data.Icon
import com.maltaisn.icondialog.pack.IconPack

const val DEFAULT_ICON_ID = 522 // Cycling icon as standard

class AddEditActivityTypeFragment : Fragment(), IconDialog.Callback {

    private var iconId: Int = DEFAULT_ICON_ID
    private lateinit var binding: FragmentAddeditactivitytypeBinding

    private lateinit var iconPack: IconPack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (userChangedSomething()
                ) {
                    DialogView(requireContext()).showYesNoDialog(
                        getString(R.string.attention),
                        getString(R.string.goBackMessage),
                        { _, _ ->
                            findNavController().popBackStack()
                        },
                        { _, _ -> }
                    )
                } else {
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun userChangedSomething() = !binding.cardioPointsValue.text.isNullOrEmpty() ||
            !binding.caloriesValue.text.isNullOrEmpty() ||
            !binding.descriptionValue.text.isNullOrEmpty() ||
            !binding.activityType.text.isNullOrEmpty()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_addeditactivitytype, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            binding.icon.setColorFilter(Color.WHITE)

        binding.close.setOnClickListener { findNavController().popBackStack() }
        handleSaveButton(powerActivity, idOfActivity, newActivity, database)
        return binding.root
    }

    private fun handleSaveButton(
        powerActivity: Boolean?,
        idOfActivity: Long?,
        newActivity: Boolean,
        database: WorkoutRepo
    ) {
        var activity: ActivityType
        binding.save.setOnClickListener {
            if (binding.activityType.text.isNullOrEmpty()
                || binding.caloriesValue.text.isNullOrEmpty()
                || binding.cardioPointsValue.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    R.string.pleaseInsertAllDataForTheActivity,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                activity = ActivityType(
                    binding.activityType.text.toString(),
                    iconId,
                    binding.descriptionValue.text.toString(),
                    powerActivity!!,
                    binding.caloriesValue.text.toString().toDouble(),
                    binding.cardioPointsValue.text.toString().toDouble()
                )
                activity.id = idOfActivity
                if (!newActivity && idOfActivity != null) {
                    database.updateActivityType(activity)
                } else {
                    activity.id = null
                    database.addActivityType(activity)
                }
                findNavController().popBackStack()
            }
        }
    }

    override val iconDialogIconPack: IconPack
        get() = iconPack

    override fun onIconDialogIconsSelected(dialog: IconDialog, icons: List<Icon>) {
        iconId = icons.first().id
        binding.icon.setImageDrawable(iconPack.getIcon(iconId)!!.drawable)
    }
}
