package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView


fun Activity.CommonDialog(context: Context) { }

fun Activity.appNotFound1(context: Context, serviceName: String?) {
    val dialog = Dialog(context, R.style.DialogTheme)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_update)
    dialog.window!!.setBackgroundDrawable(
        ColorDrawable(
            context.resources
                .getColor(R.color.colorTransparent)
        )
    )
    dialog.show()
    val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
    val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
    val dialogOK = dialog.findViewById<TextView>(R.id.dialogOK)
    val dialogCancel = dialog.findViewById<TextView>(R.id.dialogCancel)
    dialogCancel.visibility = View.GONE
    dialogOK.text = "Close"
    dialogTitle.text = "App not found!"
    dialogInfo.text =
        "Please install " + serviceName + " application to view this content. Please install the application from home page."
    dialogOK.setOnClickListener {
        (context as Activity).finish()
    }
}

fun Activity.noActiveSubscription1(context: Context, serviceName: String?) {
    val dialog = Dialog(context, R.style.DialogTheme)
    dialog.setContentView(R.layout.dialog_update)
    dialog.window!!.setBackgroundDrawable(
        ColorDrawable(
            context.resources.getColor(
                R.color.colorTransparent
            )
        )
    )
    dialog.show()
    val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
    val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
    val dialogCancel = dialog.findViewById<Button>(R.id.dialogCancel)
    dialogCancel.visibility = View.GONE
    val dialogOk = dialog.findViewById<Button>(R.id.dialogOK)
    dialogTitle.text = "No Active Subscription!"
    dialogInfo.text =
        "You don't have an active " + serviceName + " premium subscription. " + "Visit the 'Offers' section of the TFS Play mobile app to subscribe to one of our packs"
    dialogOk.text = "OK"
    dialogOk.setOnClickListener { (context as Activity).finish() }
}

fun Activity.plexigoMovie(context: Context) {
    val dialog = Dialog(context, R.style.DialogTheme)
    dialog.setContentView(R.layout.dialog_update)
    dialog.window!!.setBackgroundDrawable(
        ColorDrawable(
            context.resources.getColor(
                R.color.colorTransparent
            )
        )
    )
    dialog.show()
    val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
    val dialogInfo = dialog.findViewById<TextView>(R.id.dialogInfo)
    val dialogCancel = dialog.findViewById<Button>(R.id.dialogCancel)
    dialogCancel.visibility = View.GONE
    val dialogOk = dialog.findViewById<Button>(R.id.dialogOK)
    dialogTitle.text = getString(R.string.no_active_subscription)
//    dialogInfo.text = R.string.ppv_payment
    dialogInfo.text = getString(R.string.ppv_payment)
        dialogOk.text = "OK"
    dialogOk.setOnClickListener {
        dialog.dismiss()
    }
}