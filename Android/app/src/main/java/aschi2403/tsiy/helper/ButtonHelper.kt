package aschi2403.tsiy.helper

import android.view.View
import android.widget.Button
import aschi2403.tsiy.R

class ButtonHelper {
    fun configureDeleteButton(
        androidActivity: android.app.Activity,
        onclickListener: View.OnClickListener?
    ) {
        val deleteButton = androidActivity.findViewById<Button>(R.id.deleteButtonAppBar)
        deleteButton.visibility = View.VISIBLE
        deleteButton.setOnClickListener(onclickListener)
    }
}