package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.getDeviceID
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.getDeviceName
import `in`.fiberstory.tfsplaytv.model.CommanModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.utility.CustomNumKeypad
import `in`.fiberstory.tfsplaytv.utility.CustomProgressDialog
import `in`.fiberstory.tfsplaytv.utility.NoImeEditText
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.hbb20.CountryCodePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader


class ProfileActivity : FragmentActivity() {

    var btn_forgot: Button? = null
    var btn_login: Button? = null
    var btn_auth: Button? = null
    var keyPad: CustomNumKeypad? = null
    private var selected_field = "email"
    var edt_mobile: NoImeEditText? = null
    var txt_link: TextView? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var customProgressDialog: CustomProgressDialog? = null
    var countryCodePicker : CountryCodePicker?=null

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_sign_in)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        findViewById<View>(R.id.layParent).background = ContextCompat.getDrawable(this,R.drawable.app_bg)
        keyPad = findViewById<View>(R.id.custom_keypad) as CustomNumKeypad
        btn_forgot = findViewById<View>(R.id.btn_forgot) as Button
        btn_login = findViewById<View>(R.id.btn_login) as Button
        txt_link = findViewById<View>(R.id.txt_link) as TextView
        keyPad?.clickedLetter?.observe(this, clickedLetterObserver)
        keyPad?.clearState?.observe(this, clearStateObserver)
        keyPad?.clearChar?.observe(this, clearCharObserver)
        keyPad?.fieldChange?.observe(this, fieldChangeListener)
        edt_mobile = findViewById<View>(R.id.edt_mobile) as NoImeEditText
        countryCodePicker = findViewById<View>(R.id.countryCodePicker) as CountryCodePicker
        selected_field = "mobile"
        edt_mobile!!.background = resources.getDrawable(R.drawable.edt_bg_selected)
        countryCodePicker!!.background = resources.getDrawable(R.drawable.edt_bg)
        keyPad!!.setNumberFocus()
        edt_mobile!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                if (keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//                    keyPad?.requestFocus()

                    countryCodePicker?.requestFocus()
                    countryCodePicker!!.background = resources.getDrawable(R.drawable.edt_bg_selected)
                    return@OnKeyListener false
                } else if (keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    btn_login!!.requestFocus()
                    return@OnKeyListener false
                }
            }
            false
        })

        edt_mobile!!.setOnClickListener {
//            selected_field = "mobile"
//            edt_mobile!!.background = resources.getDrawable(R.drawable.edt_bg_selected)
//            keyPad!!.setNumberFocus()
        }

        btn_login!!.setOnClickListener {
            val mobile = edt_mobile!!.text.toString().trim()
            if (mobile != null && !mobile.equals("", ignoreCase = true)) {
                if (mobile.length == 10) {
                    callLoginAPI(mobile)
                } else {
                    Toast.makeText(
                        this@ProfileActivity, "Please Enter Valid Mobile No", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                edt_mobile!!.error = "Please enter Mobile"
            }
        }

        countryCodePicker!!.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                if (keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    keyPad?.requestFocus()
                    countryCodePicker!!.background = resources.getDrawable(R.drawable.edt_bg)
                    return@OnKeyListener false
                }else if(keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                    countryCodePicker!!.launchCountrySelectionDialog()
                    return@OnKeyListener false
                }else if(keyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                    countryCodePicker!!.launchCountrySelectionDialog()
                    return@OnKeyListener false
                }else if(keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    countryCodePicker!!.background = resources.getDrawable(R.drawable.edt_bg)
                    return@OnKeyListener false
                }
                else if (keyEvent.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    countryCodePicker!!.background = resources.getDrawable(R.drawable.edt_bg)
                    btn_login!!.requestFocus()
                    return@OnKeyListener false
                }
            }
            false
        })
    }

    private fun callLoginAPI(mobile: String) {
        Showloader()
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginCall: Call<String> = apiInterface.getLogin(
            mobile,countryCodePicker!!.selectedCountryCode,
            getDeviceID(),
            getDeviceName(),
            "fp04nY2bmrdQW5elPhx45v:APA91bFr85Gnx3YkBLkl4yE3tQuZ8XQTRlvBctnxwg1Hqwj8SB4iR1ubiJOUltMY7R14IUz-BhTDWmOkqQDC_bxv2RTJFcSwI_dHxmlQc3p5gDgoVUUtm1ubPxXbT8SFaEO2W6QETmv4",
            "1"
        )

        loginCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                HideLoader()
                if (response.isSuccessful) {
                    try {
                        val gson = Gson()
                        val reader: Reader = StringReader(response.body().toString())
                        val commanModel: CommanModel =
                            gson.fromJson(reader, CommanModel::class.java)
                        if (commanModel.status == 1) {
                            val intent = Intent(
                                this@ProfileActivity, VerificationActivity::class.java
                            )
                            intent.putExtra("MOBILE", mobile)
                            intent.putExtra("Country_code",countryCodePicker!!.selectedCountryCode)
                            intent.putExtra(
                                "FCMID",
                                "fp04nY2bmrdQW5elPhx45v:APA91bFr85Gnx3YkBLkl4yE3tQuZ8XQTRlvBctnxwg1Hqwj8SB4iR1ubiJOUltMY7R14IUz-BhTDWmOkqQDC_bxv2RTJFcSwI_dHxmlQc3p5gDgoVUUtm1ubPxXbT8SFaEO2W6QETmv4"
                            )
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@ProfileActivity, commanModel.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (exception: IllegalStateException) {
                        Toast.makeText(
                            this@ProfileActivity, exception.message, Toast.LENGTH_SHORT
                        ).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(
                            this@ProfileActivity, exception.message, Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        this@ProfileActivity, response.message(), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity, t.message, Toast.LENGTH_SHORT
                ).show()
                HideLoader()
            }
        })
    }

    fun Showloader() {
        customProgressDialog = CustomProgressDialog.newInstance()
        val ft = supportFragmentManager.beginTransaction()
        customProgressDialog?.show(ft, "dialog")
        customProgressDialog?.isCancelable = false
    }

    fun HideLoader() {
        if (customProgressDialog != null) customProgressDialog?.dismiss()
    }


    override fun onBackPressed() {
        super.finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

//        finish() //1
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

    }

    companion object {
        fun createIntentShow(context: Context?): Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            return intent
        }
    }

    var clearCharObserver: Observer<Boolean> = Observer<Boolean> {
        when (selected_field) {
            "mobile" -> if (edt_mobile!!.text.toString().isNotEmpty()) {
                val mobile = edt_mobile!!.text.toString()
                edt_mobile!!.setText(mobile.substring(0, mobile.length - 1))
            }
        }
    }

    var clearStateObserver: Observer<Boolean> = Observer<Boolean> {
        when (selected_field) {
            "mobile" -> edt_mobile!!.setText("")
        }
    }

    var clickedLetterObserver: Observer<String> = Observer<String> { s ->
        when (selected_field) {
            "mobile" -> {
                val mobile = edt_mobile!!.text.toString()
                if (Character.isDigit(s[0])) {
                    edt_mobile!!.setText(mobile + "" + s)
                }
            }
        }
    }

    var fieldChangeListener: Observer<Boolean> = object : Observer<Boolean> {
        override fun onChanged(aBoolean: Boolean) {
            if (aBoolean) {
                setNextFocus()
            } else {
                setPrevFocus()
            }
        }
    }

    fun setNextFocus() {
        if (selected_field == "mobile") {
            edt_mobile!!.background = resources.getDrawable(R.drawable.edt_bg)
            edt_mobile!!.clearFocus()
            btn_login!!.requestFocus()
        }
    }

    fun setPrevFocus() {}
}