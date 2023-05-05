package `in`.fiberstory.tfsplaytv.utility

import `in`.fiberstory.tfsplaytv.model.LoginDataModel
import android.content.Context
import java.io.Serializable

object UserPreferences : Serializable {
    private const val serialVersionUID = -2417702823134167882L
    private const val PREFS_NAME = "TFSPlayTV"
    private val DEFAULT_VAL: String? = null
    fun clearDB(context: Context) {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val dbEditor = prefs.edit()
        dbEditor.clear()
        dbEditor.commit()
    }

    fun setUserData(context: Context, user: LoginDataModel) {
        // TODO Auto-generated method stub
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val dbEditor = prefs.edit()
        dbEditor.putString("id", user.id)
        dbEditor.putString("device_id", user.deviceId)
        dbEditor.putString("email", user.email)
        dbEditor.putString("mobile_no", user.mobileNo)
        dbEditor.putString("gender_id", user.genderId)
        dbEditor.putString("dob", user.dob)
        dbEditor.putString("subscriber_id", user.subscriberId)
        dbEditor.putString("name", user.name)
        dbEditor.putString("profile_pic", user.profilePic)
        dbEditor.commit()
    }

    fun getUserData(context: Context): LoginDataModel {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val user = LoginDataModel()
        user.id = (prefs.getString("id", DEFAULT_VAL))
        user.deviceId = (prefs.getString("device_id", DEFAULT_VAL))
        user.email = (prefs.getString("email", DEFAULT_VAL))
        user.mobileNo = (prefs.getString("mobile_no", DEFAULT_VAL))
        user.genderId = (prefs.getString("gender_id", DEFAULT_VAL))
        user.dob = (prefs.getString("dob", DEFAULT_VAL))
        user.subscriberId = (prefs.getString("subscriber_id", DEFAULT_VAL))
        user.name = (prefs.getString("name", DEFAULT_VAL))
        user.profilePic = (prefs.getString("profile_pic", DEFAULT_VAL))
        return user
    }
}