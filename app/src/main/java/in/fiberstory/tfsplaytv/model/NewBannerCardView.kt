package `in`.fiberstory.tfsplaytv.model

import `in`.fiberstory.tfsplaytv.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.leanback.widget.BaseCardView

class NewBannerCardView(context: Context?) : BaseCardView(context) {
    var mainImageView: ImageView? = null
    var movie_name: TextView? = null
    var txtDetail: TextView? = null
    var synopsis: TextView? = null
    var layShadow: LinearLayout? = null
    protected fun buildCardView() {
        // Make sure this view is clickable and focusable
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.fragment_home_pager, this)
        mainImageView = findViewById<View>(R.id.imgBanner) as ImageView
        movie_name = findViewById<View>(R.id.movie_name) as TextView
        synopsis = findViewById<View>(R.id.synopsis) as TextView
        txtDetail = findViewById(R.id.txtDetail)
        layShadow = findViewById<View>(R.id.layShadow) as LinearLayout
        //channel_poster = findViewById(R.id.channel_poster);
        //      movie_gradient = findViewById(R.id.movie_gradient);
        isClickable = true
        isFocusable = true
        onFocusChangeListener = OnFocusChangeListener { view, b ->
            Log.e("focus", "changed")
            if (b) {
                layShadow!!.background = resources.getDrawable(R.drawable.bg_banner_selected)
                // layShadow.setVisibility(VISIBLE);
            } else {
                layShadow!!.background = null
                //layShadow.setVisibility(GONE);
            }
            /* if(b){
                        view.setBackground(getResources().getDrawable(R.drawable.bg_banner_selected));
                    }else{
                        view.setBackground(null);
                    }*/
        }
        /*setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //View container = findViewById(R.id.lay_parent);
                //view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gradient_transparent_black));
                */
        /*if(b){
                    lay_movie_border.setVisibility(VISIBLE);
                    lay_channel_border.setVisibility(VISIBLE);

                }else{
                    lay_movie_border.setVisibility(GONE);
                    lay_channel_border.setVisibility(GONE);
                }*/
        /*

                if(b){
                    lay_movie.setBackgroundDrawable(getResources().getDrawable(R.drawable.movie_bg_selected));
                }else{
                    lay_movie.setBackground(getResources().getDrawable(R.drawable.movie_bg));
                }

            }
        });*/
    }

    fun setMainImage(drawable: Drawable?) {
        mainImageView!!.setImageDrawable(drawable)
    }

    fun setTitleText(text: CharSequence?) {
        if (movie_name == null) {
            return
        }
        movie_name!!.text = text
    }

    fun setMovieInfo(text: CharSequence?) {
        if (txtDetail == null) {
            return
        }
        txtDetail!!.text = text
    }

    fun setSynopsis(text: String?) {
        if (synopsis == null) {
            return
        }
        synopsis!!.text = text
    }

    // View movie_gradient;
    init {
        buildCardView()
    }
}