package com.androijo.tvnavigation

/**
 * @author Arpit Johri
 * @createdOn Saturday, 11th July, 2020
 *
 */

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.androijo.tvnavigation.interfaces.BackPressedStateChange
import com.androijo.tvnavigation.interfaces.FragmentChangeListener
import com.androijo.tvnavigation.interfaces.NavigationStateListener
import com.androijo.tvnavigation.utils.Constants
import kotlinx.android.synthetic.main.fragment_nav_menu.*

class NavigationMenu : Fragment() {


    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var navigationStateListener: NavigationStateListener
    private lateinit var backPressedStateListener: BackPressedStateChange

    private var classTag = NavigationMenu::class.java.toString()
    private val documentaries = Constants.nav_menu_documentaries
    private val shortFilm = Constants.nav_menu_short_films
    private val profile = Constants.nav_menu_profile
    private val search = Constants.nav_menu_search
    private val home = Constants.nav_menu_home
    private val shows = Constants.nav_menu_shows
    private val news = Constants.nav_menu_tv_shows
    private val movie = Constants.nav_menu_movie
    private val podcasts = Constants.nav_menu_liveTV
    private val settings = Constants.nav_menu_settings
    private val onRentMovie = Constants.nav_menu_on_rent_movie
    private var lastSelectedMenu: String? = home
    private var profileAllowedToGainFocus = false
    private var moviesAllowedToGainFocus = false
    private var searchAllowedToGainFocus = false
    private var settingsAllowedToGainFocus = false
    private var musicAllowedToGainFocus = false
    private var onRentAllowedToFocus = false
    private var podcastsAllowedToGainFocus = false
    private var showsAllowedToGainFocus = true
    private var newsAllowedToGainFocus = false
    private var documentariesAllowedToGainFocus = false
    private var shortFilmAllowedToGainFocus = false
    private var switchUserAllowedToGainFocus = false
    private var menuTextAnimationDelay = 0//200
    private var closeNav : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_nav_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //by default selection
        setMenuIconFocusView(R.drawable.ic_home_selected, home_IB, true)

        //Navigation Menu Options Focus, Key Listeners
        searchListener()

        profileListeners()

        podcastsListeners()

        homeListeners()

       // showsListeners()

        movieListeners()

        onRentMovieListners()

        documentariesListeners()

        shortFilmListeners()

        settingsListeners()

        newsListeners()

    }

    private fun searchListener() {
        search_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(search_IB, R.drawable.ic_search_selected)
                    setMenuNameFocusView(search_TV, true)
                    focusIn(search_IB, 0)
                }

            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(search_IB, R.drawable.ic_search_unselected)
                    setMenuNameFocusView(search_TV, false)
                    focusOut(search_IB, 0)
                }
            }
        }

        search_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = search
                        fragmentChangeListener.switchFragment(search)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (!search_IB.isFocusable) search_IB.isFocusable = true
                        switchUserAllowedToGainFocus = true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = search
                        fragmentChangeListener.switchFragment(search)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }

    private fun documentariesListeners() {
        documentaries_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(documentaries_IB, R.drawable.ic_documentaries_selected)
                    setMenuNameFocusView(documentaries_TV, true)
                    focusIn(documentaries_IB, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(documentaries_IB, R.drawable.ic_documentaries_unselected)
                    setMenuNameFocusView(documentaries_TV, false)
                    focusOut(documentaries_IB, 0)
                }
            }
        }

        documentaries_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = documentaries
                        fragmentChangeListener.switchFragment(documentaries)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = documentaries
                        fragmentChangeListener.switchFragment(documentaries)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }


    private fun shortFilmListeners() {
        shortFilm_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(shortFilm_IB, R.drawable.ic_short_film_selected)
                    setMenuNameFocusView(shortFilm_TV, true)
                    focusIn(shortFilm_IB, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(shortFilm_IB, R.drawable.ic_short_film_unselected)
                    setMenuNameFocusView(shortFilm_TV, false)
                    focusOut(shortFilm_IB, 0)
                }
            }
        }

        shortFilm_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = shortFilm
                        fragmentChangeListener.switchFragment(shortFilm)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = shortFilm
                        fragmentChangeListener.switchFragment(shortFilm)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }

    private fun profileListeners() {
        profile_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(profile_IB, R.drawable.ic_profile_selected)
                    setMenuNameFocusView(profile_TV, true)
                    focusIn(profile_IB, 0)
                }

            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(profile_IB, R.drawable.ic_profile_unselected)
                    setMenuNameFocusView(profile_TV, false)
                    focusOut(profile_IB, 0)
                }
            }
        }

        profile_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = profile
                        fragmentChangeListener.switchFragment(profile)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (!profile_IB.isFocusable) profile_IB.isFocusable = true
                        switchUserAllowedToGainFocus = true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = profile
                        fragmentChangeListener.switchFragment(profile)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }

    private fun homeListeners() {

        home_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(home_IB, R.drawable.ic_home_selected)
                    setMenuNameFocusView(home_TV, true)
                    focusIn(home_IB, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(home_IB, R.drawable.ic_home_unselected)
                    setMenuNameFocusView(home_TV, false)
                    focusOut(home_IB, 0)
                }
            }
        }

        home_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = home
                        fragmentChangeListener.switchFragment(home)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (!home_IB.isFocusable) home_IB.isFocusable = true
                        switchUserAllowedToGainFocus = true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = home
                        fragmentChangeListener.switchFragment(home)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }



    private fun podcastsListeners() {

        podcasts_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(podcasts_IB, R.drawable.ic_live_tv_selected)
                    setMenuNameFocusView(podcasts_TV, true)
                    focusIn(podcasts_IB, 0)
                }

            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(podcasts_IB, R.drawable.ic_live_tv_unselected)
                    setMenuNameFocusView(podcasts_TV, false)
                    focusOut(podcasts_IB, 0)
                }
            }


            // Redraw, make the drawing order adjustment of items take effect
            val parent = v.parent as ViewGroup
            parent.requestLayout()
            parent.postInvalidate()
        }

        podcasts_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = podcasts
                        fragmentChangeListener.switchFragment(podcasts)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = podcasts
                        fragmentChangeListener.switchFragment(podcasts)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }

    private fun movieListeners() {

        movie_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(movie_IB, R.drawable.ic_movie_selected)
                    setMenuNameFocusView(movie_TV, true)
                    focusIn(movie_IB, 0)
                }

            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(movie_IB, R.drawable.ic_movie_unselected)
                    setMenuNameFocusView(movie_TV, false)
                    focusOut(movie_IB, 0)
                }
            }


            // Redraw, make the drawing order adjustment of items take effect
            val parent = v.parent as ViewGroup
            parent.requestLayout()
            parent.postInvalidate()
        }

        movie_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = movie
                        fragmentChangeListener.switchFragment(movie)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = movie
                        fragmentChangeListener.switchFragment(movie)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }

    }

    private fun onRentMovieListners() {

        onRent_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(onRent_IB, R.drawable.ic_onrent_selected)
                    setMenuNameFocusView(onRent_TV, true)
                    focusIn(onRent_IB, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(onRent_IB, R.drawable.ic_onrent_unselected)
                    setMenuNameFocusView(onRent_TV, false)
                    focusOut(onRent_IB, 0)
                }
            }
        }

        onRent_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = onRentMovie
                        fragmentChangeListener.switchFragment(onRentMovie)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (!movie_IB.isFocusable) movie_IB.isFocusable = true
                        switchUserAllowedToGainFocus = true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = onRentMovie
                        fragmentChangeListener.switchFragment(onRentMovie)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }


    private fun settingsListeners() {

        settings_IB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(settings_IB, R.drawable.ic_setting_selected)
                    setMenuNameFocusView(settings_TV, true)
                    focusIn(settings_IB, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(settings_IB, R.drawable.ic_setting_unselected)
                    setMenuNameFocusView(settings_TV, false)
                    focusOut(settings_IB, 0)
                }
            }
        }

        settings_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = settings
                        fragmentChangeListener.switchFragment(settings)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = settings
                        fragmentChangeListener.switchFragment(settings)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }

    private fun newsListeners() {

        tvShows_IB.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                if (isNavigationOpen()) {
                    setFocusedView(tvShows_IB, R.drawable.ic_tv_shows_selected)
                    setMenuNameFocusView(news_TV, true)
                    focusIn(tvShows_IB, 0)
                }
            } else {
                if (isNavigationOpen()) {
                    setOutOfFocusedView(tvShows_IB, R.drawable.ic_tv_shows_unselected)
                    setMenuNameFocusView(news_TV, false)
                    focusOut(tvShows_IB, 0)
                }
            }
        }

        tvShows_IB.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {//only when key is pressed down
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        closeNav()
                        navigationStateListener.onStateChanged(false, lastSelectedMenu)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        lastSelectedMenu = news
                        fragmentChangeListener.switchFragment(news)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        lastSelectedMenu = news
                        fragmentChangeListener.switchFragment(news)
                        closeNav()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        backPressedStateListener.backPressedStateChange()
                    }
                }
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setOutOfFocusedView(view: ImageButton, resource: Int) {
        setMenuIconFocusView(resource, view, false)
    }

    private fun setFocusedView(view: ImageButton, resource: Int) {
        setMenuIconFocusView(resource, view, true)
    }


    /**
     * Setting animation when focus is lost
     */
    fun focusOut(v: View, position: Int) {
        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1f)
        val set = AnimatorSet()
        set.play(scaleX).with(scaleY)
        set.start()
    }

    /**
     * Setting the animation when getting focus
     */
    fun focusIn(v: View, position: Int) {
        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.2f)
        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.2f)
        val set = AnimatorSet()
        set.play(scaleX).with(scaleY)
        set.start()
    }

    private fun setMenuIconFocusView(resource: Int, view: ImageButton, inFocus: Boolean) {
        view.setImageResource(resource)
    }

    private fun setMenuNameFocusView(view: TextView, inFocus: Boolean) {
        if (inFocus) {
            view.setTextColor(
                ContextCompat.getColor(
                    context!!, R.color.app_background
                )
            )
        } else view.setTextColor(
            ContextCompat.getColor(
                context!!, R.color.white
            )
        )
    }

    fun openNav() {
        closeNav = false
        enableNavMenuViews(View.VISIBLE)
        val lp = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        open_nav_CL.layoutParams = lp
        open_nav_CL.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        navigationStateListener.onStateChanged(true, lastSelectedMenu)

        when (lastSelectedMenu) {

            search -> {
                search_IB.requestFocus()
                showsAllowedToGainFocus = true
                setMenuNameFocusView(search_TV, true)
//                context!!.startActivity(Intent(context, SearchActivity::class.java))
            }
            profile -> {
                profile_IB.requestFocus()
                showsAllowedToGainFocus = true
                setMenuNameFocusView(profile_TV, true)
            }
//            shows -> {
//                shows_IB.requestFocus()
//                showsAllowedToGainFocus = true
//                setMenuNameFocusView(shows_TV, true)
//            }
            home -> {
                home_IB.requestFocus()
                moviesAllowedToGainFocus = true
                setMenuNameFocusView(home_TV, true)
            }
            podcasts -> {
                podcasts_IB.requestFocus()
                podcastsAllowedToGainFocus = true
                setMenuNameFocusView(podcasts_TV, true)
            }
            movie -> {
                movie_IB.requestFocus()
                musicAllowedToGainFocus = true
                setMenuNameFocusView(movie_TV, true)
            }
            onRentMovie -> {
                onRent_IB.requestFocus()
                onRentAllowedToFocus = true
                setMenuNameFocusView(onRent_TV , true)
            }
            news -> {
                tvShows_IB.requestFocus()
                newsAllowedToGainFocus = true
                setMenuNameFocusView(news_TV, true)
            }
            documentaries -> {
                documentaries_IB.requestFocus()
                documentariesAllowedToGainFocus = true
                setMenuNameFocusView(documentaries_TV, true)
            }
            shortFilm -> {
                shortFilm_IB.requestFocus()
                shortFilmAllowedToGainFocus = true
                setMenuNameFocusView(shortFilm_TV, true)
            }
            settings -> {
                settings_IB.requestFocus()
                settingsAllowedToGainFocus = true
                setMenuNameFocusView(settings_TV, true)
            }

        }

    }

    fun closeNav() {
        closeNav = true
        enableNavMenuViews(View.GONE)
        val lp = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        open_nav_CL.layoutParams = lp
        Constants.backCount = 0

        //highlighting last selected menu icon
        highlightMenuSelection(lastSelectedMenu)

        //Setting out of focus views for menu icons, names
        unHighlightMenuSelections(lastSelectedMenu)

    }

    private fun unHighlightMenuSelections(lastSelectedMenu: String?) {
        if (!lastSelectedMenu.equals(search, true)) {
            setOutOfFocusedView(search_IB, R.drawable.ic_search_unselected)
            setMenuNameFocusView(search_TV, false)
        }
        if (!lastSelectedMenu.equals(profile, true)) {
            setOutOfFocusedView(profile_IB, R.drawable.ic_profile_unselected)
            setMenuNameFocusView(profile_TV, false)
        }
        if (!lastSelectedMenu.equals(home, true)) {
            setOutOfFocusedView(home_IB, R.drawable.ic_home_unselected)
            setMenuNameFocusView(home_TV, false)
        }
//        if (!lastSelectedMenu.equals(shows, true)) {
//            setOutOfFocusedView(shows_IB, R.drawable.ic_onrent_unselected)
//            setMenuNameFocusView(shows_TV, false)
//        }
        if (!lastSelectedMenu.equals(podcasts, true)) {
            setOutOfFocusedView(podcasts_IB, R.drawable.ic_live_tv_unselected)
            setMenuNameFocusView(podcasts_TV, false)
        }
        if (!lastSelectedMenu.equals(movie, true)) {
            setOutOfFocusedView(movie_IB, R.drawable.ic_movie_unselected)
            setMenuNameFocusView(movie_TV, false)
        }
        if(!lastSelectedMenu.equals(onRentMovie,true)){
            setOutOfFocusedView(onRent_IB,R.drawable.ic_onrent_unselected)
            setMenuNameFocusView(onRent_TV,false)
        }
        if (!lastSelectedMenu.equals(news, true)) {
            setOutOfFocusedView(tvShows_IB, R.drawable.ic_tv_shows_unselected)
            setMenuNameFocusView(news_TV, false)
        }
        if (!lastSelectedMenu.equals(documentaries, true)) {
            setOutOfFocusedView(documentaries_IB, R.drawable.ic_documentaries_unselected)
            setMenuNameFocusView(documentaries_TV, false)
        }
        if (!lastSelectedMenu.equals(shortFilm, true)) {
            setOutOfFocusedView(shortFilm_IB, R.drawable.ic_short_film_unselected)
            setMenuNameFocusView(shortFilm_TV, false)
        }
        if (!lastSelectedMenu.equals(settings, true)) {
            setOutOfFocusedView(settings_IB, R.drawable.ic_setting_unselected)
            setMenuNameFocusView(settings_TV, false)
        }
    }

    private fun highlightMenuSelection(lastSelectedMenu: String?) {
        when (lastSelectedMenu) {
            search -> {
                setFocusedView(search_IB, R.drawable.ic_search_selected)
            }
            profile -> {
                setFocusedView(profile_IB, R.drawable.ic_profile_selected)
            }
            home -> {
                setFocusedView(home_IB, R.drawable.ic_home_selected)
            }
//            shows -> {
//                setFocusedView(shows_IB, R.drawable.ic_onrent_selected)
//            }
            podcasts -> {
                setFocusedView(podcasts_IB, R.drawable.ic_live_tv_selected)
            }
            movie -> {
                setFocusedView(movie_IB, R.drawable.ic_movie_selected)
            }
            onRentMovie ->{
                setFocusedView(onRent_IB , R.drawable.ic_onrent_selected)
            }
            news -> {
                setFocusedView(tvShows_IB, R.drawable.ic_tv_shows_selected)
            }
            documentaries -> {
                setFocusedView(documentaries_IB, R.drawable.ic_documentaries_selected)
            }
            shortFilm -> {
                setFocusedView(shortFilm_IB, R.drawable.ic_short_film_selected)
            }
            settings -> {
                setFocusedView(settings_IB, R.drawable.ic_setting_selected)
            }
        }
    }

    private fun enableNavMenuViews(visibility: Int) {

        if (visibility == View.GONE) {
            menuTextAnimationDelay = 0//200 //reset
            search_TV.visibility = visibility
            profile_TV.visibility = visibility
            home_TV.visibility = visibility
            news_TV.visibility = visibility
            movie_TV.visibility = visibility
            podcasts_TV.visibility = visibility
            documentaries_TV.visibility = visibility
            shortFilm_TV.visibility = visibility
            settings_TV.visibility = visibility
            onRent_TV.visibility = visibility
        } else {
            animateMenuNamesEntry(home_TV, visibility, 1)
        }

    }

    private fun animateMenuNamesEntry(view: View, visibility: Int, viewCode: Int) {
        view.postDelayed({
            view.visibility = visibility
            val animate = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_menu_name)
            view.startAnimation(animate)
            menuTextAnimationDelay = 100
            if(!closeNav) {
                when (viewCode) {
                    1 -> {
                        animateMenuNamesEntry(search_TV, visibility, viewCode + 1)
                    }
                    2 -> {
                        animateMenuNamesEntry(profile_TV, visibility, viewCode + 1)
                    }
                    3 -> {
                        animateMenuNamesEntry(podcasts_TV, visibility, viewCode + 1)
                    }
                    4 -> {
                        animateMenuNamesEntry(movie_TV, visibility, viewCode + 1)
                    }
                    5 -> {
                        animateMenuNamesEntry(onRent_TV , visibility , viewCode + 1)
                    }
                    6 -> {
                        animateMenuNamesEntry(news_TV, visibility, viewCode + 1)
                    }
                    7 -> {
                        animateMenuNamesEntry(documentaries_TV, visibility, viewCode + 1)
                    }
                    8 -> {
                        animateMenuNamesEntry(shortFilm_TV, visibility, viewCode + 1)
                    }
                    9 -> {
                        animateMenuNamesEntry(settings_TV, visibility, viewCode + 1)
                    }
                }
            }else{view.visibility = GONE }
        }, menuTextAnimationDelay.toLong())
    }

    private fun isNavigationOpen() = home_TV.visibility == View.VISIBLE

    fun setFragmentChangeListener(callback: FragmentChangeListener) {
        this.fragmentChangeListener = callback
    }

    fun setNavigationStateListener(callback: NavigationStateListener) {
        this.navigationStateListener = callback
    }

    fun setBackPressedListener(callback : BackPressedStateChange){
        this.backPressedStateListener = callback
    }

    fun setSelectedMenu(navMenuName: String) {
        when (navMenuName) {
            shows -> {
                lastSelectedMenu = shows
            }
            home -> {
                lastSelectedMenu = home
            }
        }

        highlightMenuSelection(lastSelectedMenu)
        unHighlightMenuSelections(lastSelectedMenu)

    }


}
