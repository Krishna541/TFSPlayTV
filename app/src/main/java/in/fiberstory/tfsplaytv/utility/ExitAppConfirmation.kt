package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.androijo.tvnavigation.utils.Constants

fun Activity.ExitAppConfirmation (context: Context, exitCallBack: ()->Unit) {
    val dialog = Dialog(context, R.style.DialogTheme)
    dialog.setContentView(R.layout.dialog_common)
    dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.colorTransparent)))
    dialog.show()
    val dialogTitle: TextView = dialog.findViewById(R.id.dialogTitle)
    val dialogInfo: TextView = dialog.findViewById(R.id.dialogInfo)
    val dialogCancel: Button = dialog.findViewById(R.id.dialogCancel)
    val dialogOk: Button = dialog.findViewById(R.id.dialogOk)
    dialogTitle.setText(    R.string.attention)
    dialogInfo.setText(R.string.exit_alert_message)
    dialogOk.setText(R.string.Proceed)
    dialogCancel.setText(R.string.Cancel)
    dialogCancel.setOnClickListener {
        Constants.backCount = 1
        dialog.dismiss()

    }
    dialogOk.setOnClickListener {
        dialog.dismiss()
        exitCallBack()

    }


}