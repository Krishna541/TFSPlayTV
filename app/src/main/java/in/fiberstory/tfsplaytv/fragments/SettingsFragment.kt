package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.getDeviceID
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.getDeviceName
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.savePrefs
import `in`.fiberstory.tfsplaytv.activities.ProfileActivity
import `in`.fiberstory.tfsplaytv.activities.WebActivity
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import `in`.fiberstory.tfsplaytv.model.CommanModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import `in`.fiberstory.tfsplaytv.utility.UserPreferences
import `in`.fiberstory.tfsplaytv.utility.logoutAlert
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.leanback.app.RowsSupportFragment
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader


class SettingsFragment : RowsSupportFragment() {

    private var lyl_privacy: LinearLayout? = null
    private var lyl_terms_of_use: LinearLayout? = null
    private var lyl_help_support: LinearLayout? = null
//    private var lyl_logout: LinearLayout? = null
    private var btnLoginRegister: Button? = null
    private var txtVerifiedName: TextView? = null
    private var user_not_loggedIn_ConstraintLayout: ConstraintLayout? = null
    private var user_Showdetails_ConstraintLayout: ConstraintLayout? = null
    private var help_and_support_ConstraintLayout: ConstraintLayout? = null
    private var lyl_containers: ConstraintLayout? = null
    private var btnOk: Button? = null
    private lateinit var navigationMenuCallback: NavigationMenuCallback
    lateinit var sharedPreferences: SharedPreferences
    private var flag: Boolean = false
    lateinit var editor: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView: View = inflater.inflate(R.layout.fragment_setting, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        initView(rootView)
        return rootView
    }


    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        workaroundFocus()
        /*
      Rest of the code
    */
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(rootView: View) {
        user_not_loggedIn_ConstraintLayout =
            rootView.findViewById(R.id.user_not_loggedIn_ConstraintLayout)
        user_Showdetails_ConstraintLayout =
            rootView.findViewById(R.id.user_Showdetails_ConstraintLayout)
        help_and_support_ConstraintLayout =
            rootView.findViewById(R.id.help_and_support_ConstraintLayout)
        btnOk = rootView.findViewById(R.id.btnOk)
        lyl_containers = rootView.findViewById(R.id.lyl_containers)
        txtVerifiedName = rootView.findViewById(R.id.txtVerifiedName)
        lyl_privacy = rootView.findViewById(R.id.lyl_privacy)
        lyl_terms_of_use = rootView.findViewById(R.id.lyl_terms_of_use)
        lyl_help_support = rootView.findViewById(R.id.lyl_help_support)
//        lyl_logout = rootView.findViewById(R.id.lyl_logout)
        btnLoginRegister = rootView.findViewById(R.id.btnLoginRegister)

        lyl_privacy?.requestFocus()

//        if (CheckUserLogin.loadPrefs(activity) == 1) {
            user_not_loggedIn_ConstraintLayout?.visibility = View.GONE
            user_Showdetails_ConstraintLayout?.visibility = View.VISIBLE
//            txtVerifiedName?.text = (activity?.let { UserPreferences.getUserData(it).name })
//        }
        /*
        else {
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
        */

        lyl_privacy?.setOnClickListener {
            val intent = Intent(
                activity, WebActivity::class.java
            )
            intent.putExtra("DATA", "https://app.secure.tfsplay.in/web/privacy_policy.html")
            startActivity(intent)

        }
        lyl_terms_of_use?.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                activity, WebActivity::class.java
            )
            intent.putExtra("DATA", "https://app.secure.tfsplay.in/web/tnc.html")
            startActivity(intent)
        })

        lyl_help_support?.setOnClickListener {
            help_and_support_ConstraintLayout?.visibility = View.VISIBLE
            user_Showdetails_ConstraintLayout?.visibility = View.GONE
            btnOk?.requestFocus()
            flag = true
            btnOk?.setOnClickListener {
                flag = false
                lyl_help_support?.requestFocus()
                help_and_support_ConstraintLayout?.visibility = View.GONE
                user_Showdetails_ConstraintLayout?.visibility = View.VISIBLE
            }


            btnOk?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_BACK -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false
                    }

                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        btnOk?.requestFocus()
                    }


                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false

                    }
                }
            }
            false
        })



        }


/*

        lyl_logout?.setOnClickListener {
            activity?.logoutAlert(requireActivity()) {
                Handler().postDelayed({ selfLogout() }, 700)
            }
        }
*/

        btnLoginRegister!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_BACK -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false
                    }
                }
            }

            false
        })


        lyl_privacy!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_BACK -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {

                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false

                    }
                }
            }
            false
        })


        lyl_help_support!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_BACK -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {

                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false

                    }
                }
            }
            false
        })

/*
        lyl_logout!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_BACK -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {

                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false

                    }
                }
            }
            false
        })

        */

        lyl_terms_of_use!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_BACK -> {
                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {

                        navigationMenuCallback.navMenuToggle(true)
                        return@OnKeyListener false

                    }
                }
            }
            false
        })



    }

    override fun onResume() {
        super.onResume()
    }

    fun setNavigationMenuCallback(callback: NavigationMenuCallback) {
        this.navigationMenuCallback = callback
    }

    private fun selfLogout() {
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginCall: Call<String> = apiInterface.getSelfLogout(
            activity?.let { UserPreferences.getUserData(it).subscriberId },
            getDeviceID(),
            getDeviceName(),
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
                            savePrefs("pref_login", 0)
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

    fun restoreFocusOnPrivacy() {
        lyl_privacy?.requestFocus()

    }
    fun restoreFocusOnHelpOkBtn() {
        btnOk?.requestFocus()

    }

    fun getFlag(): Boolean {
        return flag
    }


}