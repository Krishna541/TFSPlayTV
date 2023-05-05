package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity
import `in`.fiberstory.tfsplaytv.activities.ProfileActivity
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.CommanModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import `in`.fiberstory.tfsplaytv.utility.logoutAlert
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.leanback.app.RowsSupportFragment
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class ProfileFragment : RowsSupportFragment() {


    private lateinit var navigationMenuCallback: NavigationMenuCallback
    private var txtVerifiedName: TextView? = null
    private var txtLoginEmail: TextView? = null
    private var txtLoginMobile: TextView? = null
    private var txtLoginID: TextView? = null
    private var lyl_logout: LinearLayout? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var user_Showdetails_ConstraintLayout: ConstraintLayout? = null
    private var user_not_loggedIn_ConstraintLayout: ConstraintLayout? = null
    private var btnLoginRegister: Button? = null


    private var constraintLayout: ConstraintLayout? = null
    private var rootView: View? = null


    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val rootView: View = inflater.inflate(R.layout.fragment_profile, container, false)
        initView(rootView)
        return rootView
    }

    private fun setProfileData(rootView: View) {
        if (CheckUserLogin.loadPrefs(activity) == 1) {
            constraintLayout = rootView.findViewById(R.id.user_Showdetails_ConstraintLayout)
            constraintLayout?.visibility = View.VISIBLE
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                activity?.let { ContextCompat.getColor(it, R.color.app_background) }?.let {
                    activity?.let { ContextCompat.getColor(it, R.color.app_background) }
                        ?.let { it1 ->
                            activity?.let { ContextCompat.getColor(it, R.color.colorAccent) }
                                ?.let { it2 ->
                                    activity?.let {
                                        ContextCompat.getColor(
                                            it,
                                            R.color.colorAccent
                                        )
                                    }
                                        ?.let { it3 ->
                                            activity?.let {
                                                ContextCompat.getColor(
                                                    it,
                                                    R.color.app_background
                                                )
                                            }
                                                ?.let { it4 ->
                                                    intArrayOf(
                                                        it,
                                                        it1,
                                                        it2,
                                                        it3,
                                                        it4
                                                    )
                                                }
                                        }
                                }
                        }
                }
            )
            //constraintLayout?.background = gradientDrawable
            txtVerifiedName = rootView.findViewById(R.id.txtVerifiedName)
            txtLoginEmail = rootView.findViewById(R.id.txtLoginEmail)
            txtLoginMobile = rootView.findViewById(R.id.txtLoginMobile)
            txtLoginID = rootView.findViewById(R.id.txtLoginID)
            lyl_logout = rootView.findViewById(R.id.lyl_logout)
            btnLoginRegister = rootView.findViewById(R.id.btnLoginRegister)
            user_Showdetails_ConstraintLayout =
                rootView.findViewById(R.id.user_Showdetails_ConstraintLayout)
            user_not_loggedIn_ConstraintLayout =
                rootView.findViewById(R.id.user_not_loggedIn_ConstraintLayout)
            txtVerifiedName?.text = (activity?.let { UserPreferences.getUserData(it).name })
            txtLoginEmail?.text = (activity?.let { UserPreferences.getUserData(it).email })
            txtLoginMobile?.text = activity?.let { UserPreferences.getUserData(it).mobileNo }
            txtLoginID?.text = activity?.let { UserPreferences.getUserData(it).subscriberId }
        } else {
            activity?.startActivity(
                ProfileActivity.createIntentShow(
                    activity
                )
            )
//            activity?.finish()
        }
        this.rootView = rootView

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileData(view)
        if (CheckUserLogin.loadPrefs(activity) == 1) {
            lyl_logout?.requestFocus()

            lyl_logout?.setOnClickListener {
                activity?.logoutAlert(requireActivity()) {
                    Handler().postDelayed({ selfLogout() }, 700)
                }
            }

            lyl_logout!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (i) {
                        KeyEvent.KEYCODE_BACK -> {
                            navigationMenuCallback.navMenuToggle(true)
                            return@OnKeyListener false
                        }
                        KeyEvent.KEYCODE_DPAD_LEFT -> {

                            lyl_logout?.setBackgroundResource(R.drawable.round_solid_gray)

                            navigationMenuCallback.navMenuToggle(true)
                            return@OnKeyListener false

                        }
                        KeyEvent.KEYCODE_DPAD_RIGHT -> {

                            lyl_logout?.setBackgroundResource(R.drawable.round_solid_grey_logout_btn)
                            lyl_logout?.requestFocus()

                            return@OnKeyListener false

                        }
                    }
                }
                false
            })
        }
//        else{
//            user_Showdetails_ConstraintLayout?.visibility = View.VISIBLE
//        }

        view.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        startActivity(
                            Intent(
                                activity, MainActivity::class.java
                            )
                        )

                    }
                    KeyEvent.KEYCODE_BACK -> {
                        activity?.finish()
                    }

/*
                    KeyEvent.KEYCODE_BACK -> {

                        requireActivity()
                            .onBackPressedDispatcher
                            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                                override fun handleOnBackPressed() {
                                    // Do custom work here
                                    navigationMenuCallback.navMenuToggle(true)
                                }
                            })
                    }
                    */
                }
            }
            true
        }
    }


    private fun initView(rootView: View) {


        if (CheckUserLogin.loadPrefs(activity) == 1) {
            user_Showdetails_ConstraintLayout?.visibility = View.VISIBLE
            user_not_loggedIn_ConstraintLayout?.visibility = View.GONE

//            txtVerifiedName?.text = (activity?.let { UserPreferences.getUserData(it).name })
        } else {
            user_Showdetails_ConstraintLayout?.visibility = View.GONE
            user_not_loggedIn_ConstraintLayout?.visibility = View.VISIBLE

            btnLoginRegister?.requestFocus()
            btnLoginRegister?.setOnClickListener {
                startActivity(
                    ProfileActivity.createIntentShow(
                        activity
                    )
                )
            }

        }


    }

    private fun selfLogout() {
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginCall: Call<String> = apiInterface.getSelfLogout(
            activity?.let { UserPreferences.getUserData(it).subscriberId },
            MainActivity.getDeviceID(),
            MainActivity.getDeviceName(),
            "1"
        )
        loginCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    try {
                        val gson = Gson()
                        val reader: Reader = StringReader(response.body().toString())
                        val commanModel: CommanModel =
                            gson.fromJson(reader, CommanModel::class.java)
                        if (commanModel.status === 1) {
                            editor = sharedPreferences.edit()
                            editor.putString("isNewMovieBannerClicked", "")
                            editor.apply()
                            activity?.let { UserPreferences.clearDB(it) }
                            MainActivity.savePrefs("pref_login", 0)
                            startActivity(Intent(activity, MainActivity::class.java))
                            activity!!.finish()
                        } else {
                            Toast.makeText(
                                activity, "" + commanModel.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (exception: IllegalStateException) {
                        Toast.makeText(
                            activity, "" + exception.message.toString(), Toast.LENGTH_SHORT
                        ).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(
                            activity, "" + exception.message.toString(), Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity, "" + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun restoreDrawableFocusOnLogout() {
        lyl_logout?.setBackgroundResource(R.drawable.round_solid_grey_logout_btn)

    }


}