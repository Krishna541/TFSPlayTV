package `in`.fiberstory.tfsplaytv.utility

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

@SuppressLint("AppCompatCustomView")
class NoImeEditText(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
    override fun onCheckIsTextEditor(): Boolean {
        return false
    }
}