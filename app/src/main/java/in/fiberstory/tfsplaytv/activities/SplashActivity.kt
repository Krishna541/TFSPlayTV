package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.BuildConfig
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.model.AppVersionModel
import `in`.fiberstory.tfsplaytv.model.Data
import `in`.fiberstory.tfsplaytv.network.APIClient
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.utility.CheckUserLogin
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader
import java.io.StringReader

class SplashActivity : FragmentActivity() {


    var appVersionDetails: Data? = null
    var isForceUpdate = false
    private var PACKAGE_NAME: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({ // This method will be executed once the timer is over

            // Start your app main activity
            val i = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(i)

            // close this activity
            finish()


            //getAppVersion()


        }, SPLASH_TIME_OUT.toLong())
    }

    companion object {
        // Splash screen timer
        private const val SPLASH_TIME_OUT = 3000
    }



    private fun getAppVersion() {
        val apiInterface: APIInterface =
            APIClient.getClient().create<APIInterface>(APIInterface::class.java)
        val apiCall: Call<String> = apiInterface.getAppVersion()
        apiCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        try {
                            val gson = Gson()
                            val reader: Reader = StringReader(response.body().toString())
                            val appVersionModel: AppVersionModel = gson.fromJson(
                                reader,
                                AppVersionModel::class.java
                            )
                            val appVersionResponse: ArrayList<Data> =
                                appVersionModel.data
//
                            PACKAGE_NAME = applicationContext.packageName

                            if (appVersionResponse != null && appVersionResponse.size > 0) {
                                for (i in appVersionResponse.indices) {
                                    appVersionResponse[i].app_name
                                    //if (appVersionResponse.get(i).getAppName().equalsIgnoreCase("TFS Play Android")) {
                                    if (appVersionResponse[i].package_id?.trim()
                                            .equals(PACKAGE_NAME.toString().trim { it <= ' ' })
                                    ) {
                                        if (appVersionResponse != null && appVersionResponse.size > 0) {
                                            appVersionDetails = appVersionResponse[i]
//                                            Log.i("sunil_", "onResponse - appVersionResponse: $appVersionDetails")


                                            if (BuildConfig.VERSION_NAME.toDouble() >=
                                                appVersionDetails!!.app_version
                                                    ?.trim()!!.toDouble()
                                            ) {
//                                                if (CheckUserLogin.loadPrefs(this@SplashActivity) === 1) {
                                                    navigateToDashboard()
//                                                }
                                                //==================== navigate
                                                /*
                                                else {
//                                                    NavigateToLoginActivity.navigate(
//                                                        this@SplashActivity,
//                                                        1
//                                                    )
                                                    startActivity(
                                                        ProfileActivity.createIntentShow(
                                                            this@SplashActivity
                                                        )
                                                    )
                                                }
                                                */
                                                //==================
                                            }
                                            else {
                                                if (appVersionDetails!!.mandatory?.trim()
                                                        .equals("1")) {
                                                    isForceUpdate = true
                                                } else {
                                                    isForceUpdate = false
                                                }
                                                showForceUpdateDialog()
                                            }

                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            this@SplashActivity,
                            "" + response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (exception: IllegalStateException) {
                    Toast.makeText(
                        this@SplashActivity,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: JsonSyntaxException) {
                    Toast.makeText(
                        this@SplashActivity,
                        "" + exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SplashActivity, "" + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    fun navigateToDashboard() {
        val i = Intent()
        i.setClass(this@SplashActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun showForceUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Available!")
        builder.setMessage("Please update your app to the latest available version.")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Update"
        ) { dialog, id -> navigateToPlayStore() }
        if (isForceUpdate == false) {
            builder.setNegativeButton(
                "Skip"
            ) { dialog, id -> //                            navigateToDashboard();
                if (CheckUserLogin.loadPrefs(this@SplashActivity) === 1) {
                    navigateToDashboard()
                } else {
//                    NavigateToLoginActivity.navigate(this@SplashActivity, 1)
                    startActivity(
                        ProfileActivity.createIntentShow(
                            this@SplashActivity
                        )
                    )
                }
            }
        }
        val alert = builder.create()
        alert.show()
        val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        nbutton.setTextColor(resources.getColor(R.color.colorAccent))
        val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        pbutton.setTextColor(resources.getColor(R.color.colorAccent))
    }


    fun navigateToPlayStore() {
        val appPackageName =
            packageName // getPackageName() from Context or Activity object
        Log.i("sunil_", "navigateToPlayStore:$appPackageName ")
//        val static_packageid = "in.fiberstory.tvlauncher"
//        val static_url_check = "market://details?id=in.fiberstory.tvlauncher"
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(

                        "market://details?id=$appPackageName"
                    )
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://play.google.com/store/apps/details?id=$appPackageName"
                    )
                )
            )
        }
    }


}