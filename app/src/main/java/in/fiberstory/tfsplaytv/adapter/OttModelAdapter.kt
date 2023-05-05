package `in`.fiberstory.tfsplaytv.adapter

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.model.OTTModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class OttModelAdapter(movies: ArrayList<OTTModel>?, context: Context, enableFocus: Boolean) :
    RecyclerView.Adapter<OttModelAdapter.OttHolder>() {
    var context: Context
    var movies = ArrayList<OTTModel>()
    var listener: OnOttClickListener
    var enableFocus: Boolean

    interface OnOttClickListener {
        fun onOttClicked(model: OTTModel?)
    }

    inner class OttHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtOtt: TextView
        var layParent: RelativeLayout
        var ivOtt: ImageView

        init {
            txtOtt = itemView.findViewById<View>(R.id.txtOtt) as TextView
            ivOtt = itemView.findViewById<View>(R.id.ivOtt) as ImageView
            layParent = itemView.findViewById<View>(R.id.layParent) as RelativeLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OttHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ott, parent, false)
        return OttHolder(itemView)
    }

    override fun onBindViewHolder(holder: OttHolder, position: Int) {
        val model = movies[position]
        /*if(model.getOttPlatform().equalsIgnoreCase("plexigo")){
            if(model.getInr().equalsIgnoreCase("0")||model.getInr().equalsIgnoreCase("-1")){
                holder.txtOtt.setText("Watch on ");
            }else {
                holder.txtOtt.setText(Html.fromHtml(model.getButtonText() + " Rs. " + model.getInr() + " on "));
            }
        }else {
            holder.txtOtt.setText(Html.fromHtml("Watch on "));
        }*/holder.txtOtt.setText(model.buttonText)
        if (model.imagePath != null && !model.imagePath.equals("",ignoreCase = true)) {
            if (model.ottPlatform.equals("plexigo",ignoreCase = true)) {
                Glide.with(context)
                    .load(model.wideImagePath)
                    .into(holder.ivOtt)
            } else {
                Glide.with(context)
                    .load(model.wideImagePath)
                    .into(holder.ivOtt)
            }
        }
        holder.layParent.setOnClickListener { listener.onOttClicked(model) }
        holder.layParent.isFocusable = enableFocus
        holder.layParent.isFocusableInTouchMode = enableFocus

        if(position == 0){
            holder.layParent.requestFocus()
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    init {
        this.movies.clear()
        this.movies.addAll(movies!!)
        this.context = context
        listener = context as OnOttClickListener
        this.enableFocus = enableFocus
        //this.listener = (PopularAdapter.OnPopularClickListener)context;
    }
}

