package `in`.fiberstory.tfsplaytv.adapter

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.SearchActivity
import android.annotation.SuppressLint
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class KeypadAdapter(context: SearchActivity, items: List<String>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val context: SearchActivity = context
    val item: List<String>? = items
    private var mItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    private fun getItem(position: Int): String {
        return item!![position]
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.item_keypad, viewGroup, false)
        return ViewHolder(v)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        if (item!!.isNotEmpty()) {
            viewHolders = viewHolder as ViewHolder
            val displayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val deviceHeight: Int = displayMetrics.heightPixels
            val deviceWidth: Int = displayMetrics.widthPixels
            if (deviceWidth == 1280 && deviceHeight == 720) {
                viewHolders!!.txtKeypad.layoutParams.width = 40
                viewHolders!!.txtKeypad.layoutParams.height = 40
            } else {
                viewHolders!!.txtKeypad.layoutParams.width = 60
                viewHolders!!.txtKeypad.layoutParams.height = 60
            }
            viewHolders!!.txtKeypad.text = item[position]
            viewHolders!!.txtKeypad.setOnClickListener(View.OnClickListener { v ->
                if (mItemClickListener != null) {
                    mItemClickListener!!.onItemClick(v, position)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return item!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtKeypad: TextView = itemView.findViewById<TextView>(R.id.txtNumber)

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var viewHolders: ViewHolder? = null
    }

}