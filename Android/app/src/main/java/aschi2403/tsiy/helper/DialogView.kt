package aschi2403.tsiy.helper

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import aschi2403.tsiy.R

class DialogView(val context: Context) {

    var checkedItem: Int = 0

    fun showYesNoDialog(
        title: String,
        message: String,
        yes: DialogInterface.OnClickListener,
        no: DialogInterface.OnClickListener
    ) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("YES", yes)
        alertDialog.setNegativeButton(
            "NO", no
        )
        alertDialog.create().show()
    }
    fun showItemCheckDialog(
        title: Int,
        items: List<String>,
        ok: DialogInterface.OnClickListener,
        cancel: DialogInterface.OnClickListener
    ) {
        val itemCheckDialog = AlertDialog.Builder(context)
        itemCheckDialog.setTitle(title)

        checkedItem = 0 // first
        itemCheckDialog.setSingleChoiceItems(items.toTypedArray(), checkedItem) { _, which ->
            checkedItem = which
            // user checked an item
        }

        itemCheckDialog.setPositiveButton(R.string.ok, ok)
        itemCheckDialog.setNegativeButton(R.string.cancel, cancel)


        itemCheckDialog.create().show()
    }

}