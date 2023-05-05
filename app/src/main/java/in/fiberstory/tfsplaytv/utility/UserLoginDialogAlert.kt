package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.ProfileActivity
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView


fun Activity.userLoginDialogAlert(context: Context) {
    val dialog = Dialog(context, R.style.DialogTheme)
    dialog.setContentView(R.layout.dialog_common)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorTransparent)))
    dialog.show()
    val dialogTitle: TextView = dialog.findViewById(R.id.dialogTitle)
    val dialogInfo: TextView = dialog.findViewById(R.id.dialogInfo)
    val dialogCancel: Button = dialog.findViewById(R.id.dialogCancel)
    val dialogOk: Button = dialog.findViewById(R.id.dialogOk)
    dialogTitle.setText(R.string.attention)
    dialogInfo.setText(R.string.Login_Register_Alert_Message)
    dialogOk.setText(R.string.Proceed)
    dialogCancel.setText(R.string.Cancel)
    dialogCancel.setOnClickListener {
        dialog.dismiss()

    }
    dialogOk.setOnClickListener {
        dialog.dismiss()
        startActivity(ProfileActivity.createIntentShow(this))

    }
}