package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class CustomProgressDialog : DialogFragment() {
    override fun onStart() {
        super.onStart()
        val w = dialog!!.window
        //0 means transparent outside
        //1 means black outside
        w!!.setDimAmount(0f)
        w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_progress_layout, container, false)
    }

    companion object {
        fun newInstance(): CustomProgressDialog {

            // Supply num input as an argument.
            return CustomProgressDialog()
        }
    }
}