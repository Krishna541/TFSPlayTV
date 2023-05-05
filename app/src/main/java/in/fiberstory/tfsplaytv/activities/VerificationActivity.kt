package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.getDeviceID
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.getDeviceName
import `in`.fiberstory.tfsplaytv.activities.MainActivity.Companion.savePrefs
import `in`.fiberstory.tfsplaytv.model.CommanModel
import `in`.fiberstory.tfsplaytv.model.LoginModel
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.utility.*
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class VerificationActivity : FragmentActivity() {
    var edt_one: NoImeEditText? = null
    var edt_two: NoImeEditText? = null
    var edt_three: NoImeEditText? = null
    var edt_four: NoImeEditText? = null
    var edt_five: NoImeEditText? = null
    var edt_six: NoImeEditText? = null
    var btn_verfiy: Button? = null
    var ReloadafterLogin = true
    var txt_title4: TextView? = null
    var txt_title6: TextView? = null
    var txt_title5: TextView? = null
    var txt_title3: TextView? = null
    var txt_title2: TextView? = null
    var keyPad: CustomNumKeypad? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var tncLink = ""
    var fromLogin = true
    var customProgressDialog: CustomProgressDialog? = null
    var countryCode : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        )

        countryCode = intent.getStringExtra("Country_code")

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                ContextCompat.getColor(this, R.color.app_background),
                ContextCompat.getColor(this, R.color.app_background),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.app_background)
            )
        )
        findViewById<View>(R.id.layParent).background = gradientDrawable
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        edt_one = findViewById<View>(R.id.edt_one) as NoImeEditText
        edt_two = findViewById<View>(R.id.edt_two) as NoImeEditText
        edt_three = findViewById<View>(R.id.edt_three) as NoImeEditText
        edt_four = findViewById<View>(R.id.edt_four) as NoImeEditText
        edt_five = findViewById<View>(R.id.edt_five) as NoImeEditText
        edt_six = findViewById<View>(R.id.edt_six) as NoImeEditText
        btn_verfiy = findViewById<View>(R.id.btn_verify) as Button
        txt_title4 = findViewById<View>(R.id.txt_title4) as TextView
        txt_title6 = findViewById<View>(R.id.txt_title6) as TextView
        txt_title5 = findViewById<View>(R.id.txt_title5) as TextView
        txt_title3 = findViewById<View>(R.id.txt_title3) as TextView
        txt_title2 = findViewById<View>(R.id.txt_title2) as TextView
        txt_title3!!.text =
            Html.fromHtml("<u><font color=''#ffffff>" + resources.getString(R.string.change_mobile) + "</font></u>")
        //SignupTcApi();

        txt_title2!!.text = Html.fromHtml(
            """<b><font color='#00A7E6'>Please type the verification code sent to </font><font color='#ffffff'>${intent.getStringExtra("MOBILE")}</font>""")

        txt_title3!!.visibility = View.GONE
        txt_title4!!.visibility = View.GONE

        keyPad = findViewById<View>(R.id.custom_keypad) as CustomNumKeypad
        keyPad?.clickedLetter?.observe(this, clickedLetterObserver)
        keyPad?.clearState?.observe(this, clearStateObserver)
        keyPad?.clearChar?.observe(this, clearCharObserver)
        keyPad?.fieldChange?.observe(this, fieldChangeListener)
        btn_verfiy!!.setOnClickListener {
            if (edt_one?.text.toString().trim() != "" && edt_two?.text.toString()
                    .trim() != "" && edt_three?.text.toString()
                    .trim() != "" && edt_four?.text.toString()
                    .trim() != "" && edt_five?.text.toString()
                    .trim() != "" && edt_six?.text.toString().trim() != ""
            ) {
                val otp: String = edt_one?.text.toString().trim() + edt_two?.text.toString()
                    .trim() + edt_three?.text.toString().trim() + edt_four?.text.toString()
                    .trim() + edt_five?.text.toString().trim() + edt_six?.text.toString().trim()
                Log.e("otp", otp)
                VerifyOtp(otp)
            } else {
                Toast.makeText(this@VerificationActivity, "Please Enter OTP", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        selected_field = "edt_one"
        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
        keyPad?.setNumberFocus()

        edt_one?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
//                    KeyEvent.KEYCODE_DPAD_CENTER -> {
//                        selected_field = "edt_one"
//                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
//                        keyPad?.setNumberFocus()
//                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_one"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> if (txt_title6!!.visibility == View.VISIBLE) {
                        txt_title6!!.requestFocus()
                    } else {
                        txt_title4!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_0 -> {
                        edt_one?.setText("0")
                        edt_one?.clearFocus()
                        edt_one?.background = resources.getDrawable(R.drawable.edt_bg)
                        edt_two?.background = resources.getDrawable(R.drawable.edt_bg_selected)
                        edt_two?.requestFocus()
                        selected_field = "edt_two"
                    }
                    KeyEvent.KEYCODE_1 -> {
                        edt_one?.setText("1")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_two?.requestFocus()
                        selected_field = "edt_two"
                    }
                    KeyEvent.KEYCODE_2 -> {
                        edt_one?.setText("2")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_two?.requestFocus()
                        selected_field = "edt_two"
                    }
                    KeyEvent.KEYCODE_3 -> {
                        edt_one?.setText("3")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                        edt_two?.requestFocus()
                    }
                    KeyEvent.KEYCODE_4 -> {
                        edt_one?.setText("4")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                        edt_two?.requestFocus()
                    }
                    KeyEvent.KEYCODE_5 -> {
                        edt_one?.setText("5")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                        edt_two?.requestFocus()
                    }
                    KeyEvent.KEYCODE_6 -> {
                        edt_one?.setText("6")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        edt_two?.requestFocus()
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                    }
                    KeyEvent.KEYCODE_7 -> {
                        edt_one?.setText("7")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                        edt_two?.requestFocus()
                    }
                    KeyEvent.KEYCODE_8 -> {
                        edt_one?.setText("8")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                        edt_two?.requestFocus()
                    }
                    KeyEvent.KEYCODE_9 -> {
                        edt_one?.setText("9")
                        //edt_two.requestFocus();
                        edt_one?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_two"
                        edt_two?.requestFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> btn_verfiy!!.requestFocus()
                    KeyEvent.KEYCODE_DPAD_LEFT -> edt_one?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_RIGHT -> edt_two?.requestFocus()
                }
            }
            false
        })
        edt_two?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        selected_field = "edt_two"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        selected_field = "edt_two"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> if (txt_title6!!.visibility == View.VISIBLE) {
                        txt_title6!!.requestFocus()
                    } else {
                        txt_title4!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> edt_one?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_RIGHT -> edt_three?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_DOWN -> btn_verfiy!!.requestFocus()
                    KeyEvent.KEYCODE_0 -> {
                        edt_two?.setText("0")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_1 -> {
                        edt_two?.setText("1")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_2 -> {
                        edt_two?.setText("2")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_3 -> {
                        edt_two?.setText("3")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_4 -> {
                        edt_two?.setText("4")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_5 -> {
                        edt_two?.setText("5")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_6 -> {
                        edt_two?.setText("6")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_7 -> {
                        edt_two?.setText("7")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_8 -> {
                        edt_two?.setText("8")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                    KeyEvent.KEYCODE_9 -> {
                        edt_two?.setText("9")
                        edt_two?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_three?.requestFocus()
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        selected_field = "edt_three?."
                    }
                }
            }
            false
        })
        edt_three?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        selected_field = "edt_three"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        selected_field = "edt_three"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> if (txt_title6!!.visibility == View.VISIBLE) {
                        txt_title6!!.requestFocus()
                    } else {
                        txt_title4!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> edt_two?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_RIGHT -> edt_four?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_DOWN -> btn_verfiy!!.requestFocus()
                    KeyEvent.KEYCODE_0 -> {
                        edt_three?.setText("0")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.background = resources.getDrawable(R.drawable.edt_bg)
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_1 -> {
                        edt_three?.setText("1")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_2 -> {
                        edt_three?.setText("2")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_3 -> {
                        edt_three?.setText("3")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_4 -> {
                        edt_three?.setText("4")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_5 -> {
                        edt_three?.setText("5")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_6 -> {
                        edt_three?.setText("6")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_7 -> {
                        edt_three?.setText("7")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_8 -> {
                        edt_three?.setText("8")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                    KeyEvent.KEYCODE_9 -> {
                        edt_three?.setText("9")
                        edt_three?.clearFocus()
                        //edt_two?.requestFocus();
                        //edt_two.setCursorVisible(true);
                        edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                        edt_four?.requestFocus()
                        selected_field = "edt_four"
                    }
                }
            }
            false
        })
        edt_four?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        selected_field = "edt_four"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        selected_field = "edt_four"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> if (txt_title6!!.visibility == View.VISIBLE) {
                        txt_title6!!.requestFocus()
                    } else {
                        txt_title4!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> edt_three?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_RIGHT -> edt_four?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_DOWN -> btn_verfiy!!.requestFocus()
                    KeyEvent.KEYCODE_0 -> {
                        edt_four?.setText("0")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_1 -> {
                        edt_four?.setText("1")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_2 -> {
                        edt_four?.setText("2")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_3 -> {
                        edt_four?.setText("3")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_4 -> {
                        edt_four?.setText("4")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_5 -> {
                        edt_four?.setText("5")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_6 -> {
                        edt_four?.setText("6")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_7 -> {
                        edt_four?.setText("7")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_8 -> {
                        edt_four?.setText("8")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_9 -> {
                        edt_four?.setText("9")
                        edt_four?.clearFocus()
                        edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                }
            }
            false
        })
        edt_five?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        selected_field = "edt_five"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        selected_field = "edt_five"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> if (txt_title6!!.visibility == View.VISIBLE) {
                        txt_title6!!.requestFocus()
                    } else {
                        txt_title4!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> edt_four?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_RIGHT -> edt_six?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_DOWN -> btn_verfiy!!.requestFocus()
                    KeyEvent.KEYCODE_0 -> {
                        edt_five?.setText("0")
                        edt_five?.clearFocus()
                        edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_1 -> {
                        edt_five?.setText("1")
                        edt_five?.clearFocus()
                        edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_2 -> {
                        edt_five?.setText("2")
                        edt_five?.clearFocus()
                        edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_3 -> {
                        edt_five?.setText("3")
                        edt_five?.clearFocus()
                        edt_five?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_4 -> {
                        edt_five?.setText("4")
                        edt_five?.clearFocus()
                        edt_five?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_5 -> {
                        edt_five?.setText("5")
                        edt_five?.clearFocus()
                        edt_five?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_6 -> {
                        edt_five?.setText("6")
                        edt_five?.clearFocus()
                        edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_7 -> {
                        edt_five?.setText("7")
                        edt_five?.clearFocus()
                        edt_five?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_8 -> {
                        edt_five?.setText("8")
                        edt_five?.clearFocus()
                        edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_9 -> {
                        edt_five?.setText("9")
                        edt_five?.clearFocus()
                        edt_five?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                }
            }
            false
        })
        edt_six?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        selected_field = "edt_five"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        selected_field = "edt_five"
                        keyPad?.setNumberFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> if (txt_title6!!.visibility == View.VISIBLE) {
                        txt_title6!!.requestFocus()
                    } else {
                        txt_title4!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> edt_five?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_RIGHT -> edt_six?.requestFocus()
                    KeyEvent.KEYCODE_DPAD_DOWN -> btn_verfiy!!.requestFocus()
                    KeyEvent.KEYCODE_0 -> {
                        edt_six?.setText("0")
                        edt_six?.clearFocus()
                        edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_1 -> {
                        edt_six?.setText("1")
                        edt_six?.clearFocus()
                        edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_2 -> {
                        edt_six?.setText("2")
                        edt_six?.clearFocus()
                        edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_3 -> {
                        edt_six?.setText("3")
                        edt_six?.clearFocus()
                        edt_six?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_4 -> {
                        edt_six?.setText("4")
                        edt_six?.clearFocus()
                        edt_six?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_5 -> {
                        edt_six?.setText("5")
                        edt_six?.clearFocus()
                        edt_six?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_6 -> {
                        edt_six?.setText("6")
                        edt_six?.clearFocus()
                        edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_7 -> {
                        edt_six?.setText("7")
                        edt_six?.clearFocus()
                        edt_six?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_8 -> {
                        edt_six?.setText("8")
                        edt_six?.clearFocus()
                        edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                        btn_verfiy!!.requestFocus()
                    }
                    KeyEvent.KEYCODE_9 -> {
                        edt_six?.setText("9")
                        edt_six?.clearFocus()
                        edt_six?.background = resources.getDrawable(R.drawable.edt_bg)
                        btn_verfiy!!.requestFocus()
                    }
                }
            }
            false
        })
        txt_title6!!.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> callLoginAPI(intent.getStringExtra("MOBILE"))

                    KeyEvent.KEYCODE_ENTER -> callLoginAPI(intent.getStringExtra("MOBILE"))
                }
            }
            false
        }
        txt_title4!!.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        //String url = "https://www.plexigo.com/terms-condition";
                        val ii = Intent(Intent.ACTION_VIEW)
                        ii.data = Uri.parse(tncLink)
                        startActivity(ii)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        //String url1 = "https://www.plexigo.com/terms-condition";
                        val `in` = Intent(Intent.ACTION_VIEW)
                        `in`.data = Uri.parse(tncLink)
                        startActivity(`in`)
                    }
                }
            }
            false
        }
//        edt_one?.requestFocus()
    }


    private fun VerifyOtp(otp: String) {
        Showloader()
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginCall: Call<String> = apiInterface.getLoginOTP(
            intent.getStringExtra("MOBILE"),countryCode,
            getDeviceID(), getDeviceName(),
            "fp04nY2bmrdQW5elPhx45v:APA91bFr85Gnx3YkBLkl4yE3tQuZ8XQTRlvBctnxwg1Hqwj8SB4iR1ubiJOUltMY7R14IUz-BhTDWmOkqQDC_bxv2RTJFcSwI_dHxmlQc3p5gDgoVUUtm1ubPxXbT8SFaEO2W6QETmv4", otp
        )

        loginCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                HideLoader()
                if (response.isSuccessful) {
                    try {
                        val gson = Gson()
                        val reader: Reader = StringReader(response.body().toString())
                        val loginModel: LoginModel = gson.fromJson(reader, LoginModel::class.java)
                        if (loginModel.status == 1) {
                            loginModel.login?.let {
                                UserPreferences.setUserData(
                                    this@VerificationActivity, it
                                )
                            }
                            savePrefs("pref_login", 1)
                            startActivity(
                                Intent(
                                    this@VerificationActivity, MainActivity::class.java
                                )
                            )
                            finish()
                        }else{
                            Toast.makeText(
                                this@VerificationActivity, loginModel.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (exception: java.lang.IllegalStateException) {
                        Toast.makeText(
                            this@VerificationActivity, exception.message, Toast.LENGTH_SHORT
                        ).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(
                            this@VerificationActivity, exception.message, Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        this@VerificationActivity, response.message(), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    this@VerificationActivity, t.message, Toast.LENGTH_SHORT
                ).show()
                HideLoader()
            }
        })


    }

    var clearCharObserver: Observer<Boolean> = Observer {
        when (selected_field) {
            "edt_one" -> edt_one?.setText("")
            "edt_two" -> {
                edt_two?.setText("")
                edt_two?.background = resources.getDrawable(R.drawable.edt_bg)
                edt_one?.background = resources.getDrawable(R.drawable.edt_bg_selected)
                selected_field = "edt_one"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_three" -> {
                edt_three?.setText("")
                edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_two"
                //   keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_four" -> {
                edt_four?.setText("")
                edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_three"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_five" -> {
                edt_five?.setText("")
                edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_four"
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_six" -> {
                edt_six?.setText("")
                edt_six?.background = resources.getDrawable(R.drawable.edt_bg)
                edt_five?.background = resources.getDrawable(R.drawable.edt_bg_selected)
                selected_field = "edt_five"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
        }
    }
    var clearStateObserver: Observer<Boolean> = Observer {
        when (selected_field) {
            "edt_one" -> edt_one?.setText("")
            "edt_two" -> {
                edt_two?.setText("")
                edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_one"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_three" -> {
                edt_three?.setText("")
                edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_two"
                //keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_four" -> {
                edt_four?.setText("")
                edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_three"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_five" -> {
                edt_five?.setText("")
                edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_four"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
            "edt_six" -> {
                edt_six?.setText("")
                edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_five"
                // keyPad?.hideEmailSugg();
                keyPad?.setLastNumberFocus("backspace")
            }
        }
    }
    var clickedLetterObserver = Observer<String> { s ->
        when (selected_field) {
            "edt_one" -> if (Character.isDigit(
                    s[0]
                )
            ) {
                //  if (edt_one?.getText().toString().length() == 0){
                edt_one?.setText(s)
                //edt_two.requestFocus();
                edt_one?.clearFocus()
                //edt_two?.requestFocus();
                //edt_two.setCursorVisible(true);
                edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_two"
                keyPad?.setLastNumberFocus(s)
                // keyPad?.hideEmailSugg();
                //}
            }
            "edt_two" -> if (Character.isDigit(s[0])) {
                //    if(edt_two.getText().toString().length() ==0) {
                edt_two?.setText(s)
                edt_two?.clearFocus()
                //edt_two?.requestFocus();
                //edt_two.setCursorVisible(true);
                edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
                selected_field = "edt_three"
                keyPad?.setLastNumberFocus(s)
                // keyPad?.hideEmailSugg();
            }
            "edt_three" -> if (Character.isDigit(s[0])) {
                //    if(edt_three?.getText().toString().length() == 0) {
                edt_three?.setText(s)
                edt_three?.setText(s)
                edt_three?.clearFocus()
                //edt_two?.requestFocus();
                //edt_two.setCursorVisible(true);
                edt_three?.background = resources.getDrawable(R.drawable.edt_bg)
                edt_four?.background = resources.getDrawable(R.drawable.edt_bg_selected)
                selected_field = "edt_four"
                keyPad?.setLastNumberFocus(s)
                //    keyPad?.hideEmailSugg();
                //  }
            }
            "edt_four" -> if (Character.isDigit(s[0])) {
                edt_four?.setText(s)
                edt_four?.setText(s)
                edt_four?.clearFocus()
                edt_four?.background = resources.getDrawable(R.drawable.edt_bg)
                edt_five?.background = resources.getDrawable(R.drawable.edt_bg_selected)
                selected_field = "edt_five"
                keyPad?.setLastNumberFocus(s)
            }
            "edt_five" -> if (Character.isDigit(s[0])) {
                edt_five?.setText(s)
                edt_five?.setText(s)
                edt_five?.clearFocus()
                edt_five?.background = resources.getDrawable(R.drawable.edt_bg)
                edt_six?.background = resources.getDrawable(R.drawable.edt_bg_selected)
                selected_field = "edt_six"
                keyPad?.setLastNumberFocus(s)
            }
            "edt_six" -> if (Character.isDigit(s[0])) {
                //  if(edt_four?.getText().toString().length() ==0) {
                edt_six?.setText(s)
                edt_six?.clearFocus()
                edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
                btn_verfiy!!.requestFocus()
                // }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AudioFocusUtils.getFocus(this)
    }

    var fieldChangeListener = Observer<Boolean> { aBoolean ->
        if (aBoolean) {
            setNextFocus()
        } else {
            setPrevFocus()
        }
    }

    fun setNextFocus() {
        if (selected_field == "edt_one") {
            edt_one?.clearFocus()
            //edt_two?.requestFocus();
            //edt_two.setCursorVisible(true);
            edt_one?.setBackground(resources.getDrawable(R.drawable.edt_bg))
            edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg_selected))
            selected_field = "edt_two"
            keyPad?.setNumberFocus()
            //keyPad?.hideEmailSugg();
        } else if (selected_field.equals("edt_two", ignoreCase = true)) {
            edt_two?.clearFocus()
            edt_two?.setBackground(resources.getDrawable(R.drawable.edt_bg))
            edt_three?.requestFocus()
            edt_three?.setCursorVisible(true)
            selected_field = "edt_three?."
        } else if (selected_field.equals("edt_three", ignoreCase = true)) {
            edt_three?.clearFocus()
            edt_three?.setBackground(resources.getDrawable(R.drawable.edt_bg))
            edt_four?.requestFocus()
            edt_four?.setCursorVisible(true)
            selected_field = "edt_four"
        } else if (selected_field.equals("edt_four", ignoreCase = true)) {
            edt_four?.clearFocus()
            edt_four?.setBackground(resources.getDrawable(R.drawable.edt_bg))
            edt_five?.requestFocus()
            edt_five?.setCursorVisible(true)
        } else if (selected_field.equals("edt_five", ignoreCase = true)) {
            edt_five?.clearFocus()
            edt_five?.setBackground(resources.getDrawable(R.drawable.edt_bg))
            edt_six?.requestFocus()
            edt_six?.setCursorVisible(true)
            selected_field = "edt_six"
        } else if (selected_field.equals("edt_six", ignoreCase = true)) {
            edt_six?.clearFocus()
            edt_six?.setBackground(resources.getDrawable(R.drawable.edt_bg))
            btn_verfiy!!.requestFocus()
        }
    }

    fun setPrevFocus() {
        if (selected_field == "edt_six") {
            edt_five?.requestFocus()
            edt_five?.isCursorVisible = true
            selected_field = "edt_five"
        } else if (selected_field.equals("edt_five", ignoreCase = true)) {
            edt_four?.requestFocus()
            edt_four?.setCursorVisible(true)
            selected_field = "edt_four"
        } else if (selected_field.equals("edt_four", ignoreCase = true)) {
            edt_three?.requestFocus()
            edt_three?.setCursorVisible(true)
            selected_field = "edt_three"
        } else if (selected_field.equals("edt_three", ignoreCase = true)) {
            edt_two?.requestFocus()
            edt_two?.setCursorVisible(true)
            selected_field = "edt_two"
        } else if (selected_field.equals("edt_two", ignoreCase = true)) {
            edt_one?.requestFocus()
            edt_one?.setCursorVisible(true)
            selected_field = "edt_one"
        }
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

    companion object {
        private var selected_field = "edt_one"
        var customProgressDialog: CustomProgressDialog? = null
    }

    private fun callLoginAPI(mobile: String?) {
        Showloader()
        val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        val loginCall: Call<String> = apiInterface.getLogin(
            mobile,countryCode, getDeviceID(), getDeviceName(),
            "fp04nY2bmrdQW5elPhx45v:APA91bFr85Gnx3YkBLkl4yE3tQuZ8XQTRlvBctnxwg1Hqwj8SB4iR1ubiJOUltMY7R14IUz-BhTDWmOkqQDC_bxv2RTJFcSwI_dHxmlQc3p5gDgoVUUtm1ubPxXbT8SFaEO2W6QETmv4", "1"
        )

        loginCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    try {
                        HideLoader()
                        val gson = Gson()
                        val reader: Reader = StringReader(response.body().toString())
                        val commanModel: CommanModel =
                            gson.fromJson(reader, CommanModel::class.java)
                        if (commanModel.status == 1) {
                            Toast.makeText(
                                this@VerificationActivity, commanModel.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (exception: IllegalStateException) {
                        Toast.makeText(
                            this@VerificationActivity, exception.message, Toast.LENGTH_SHORT
                        ).show()
                    } catch (exception: JsonSyntaxException) {
                        Toast.makeText(
                            this@VerificationActivity, exception.message, Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        this@VerificationActivity, response.message(), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    this@VerificationActivity, t.message, Toast.LENGTH_SHORT
                ).show()
                HideLoader()
            }
        })
    }
}